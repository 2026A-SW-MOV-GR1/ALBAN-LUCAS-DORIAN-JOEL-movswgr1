import React, { useState, useEffect } from 'react';
import {
  StyleSheet,
  View,
  Text,
  TextInput,
  TouchableOpacity,
  Image,
  ScrollView,
  Linking,
  NativeModules,
  NativeEventEmitter,
  DeviceEventEmitter,
  Platform,
  SafeAreaView,
  StatusBar,
} from 'react-native';

const { CameraModule } = NativeModules;

const App = () => {
  const [activeTab, setActiveTab] = useState<'outgoing' | 'incoming'>('outgoing');

  // Estado para Módulo Saliente
  const [phoneNumber, setPhoneNumber] = useState('');
  const [capturedPhoto, setCapturedPhoto] = useState<string | null>(null);

  // Estado para Módulo Entrante
  const [receivedText, setReceivedText] = useState<string | null>(null);
  const [receivedImage, setReceivedImage] = useState<string | null>(null);
  const [statusMessage, setStatusMessage] = useState('Esperando datos externos...');

  useEffect(() => {
    // Suscripción a eventos nativos
    const textSubscription = DeviceEventEmitter.addListener('onTextReceived', (text: string) => {
      setReceivedText(text);
      setReceivedImage(null); // Regla Crítica: Limpiar imagen al recibir texto
      setStatusMessage('¡Texto Recibido!');
      setActiveTab('incoming');
    });

    const imageSubscription = DeviceEventEmitter.addListener('onImageReceived', (uri: string) => {
      setReceivedImage(uri);
      setReceivedText(null); // Regla Crítica: Limpiar texto al recibir imagen
      setStatusMessage('¡Imagen Recibida!');
      setActiveTab('incoming');
    });

    return () => {
      textSubscription.remove();
      imageSubscription.remove();
    };
  }, []);

  const handleDial = async () => {
    if (phoneNumber.trim()) {
      const url = `tel:${phoneNumber}`;
      const supported = await Linking.canOpenURL(url);
      if (supported) {
        await Linking.openURL(url);
      }
    }
  };

  const handleTakePhoto = async () => {
    try {
      const base64Image = await CameraModule.takePhoto();
      setCapturedPhoto(`data:image/jpeg;base64,${base64Image}`);
    } catch (error) {
      console.error("Error al tomar foto:", error);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#F5F5F5" />

      {/* Header / Tabs */}
      <View style={styles.tabContainer}>
        <TouchableOpacity
          style={[styles.tab, activeTab === 'outgoing' && styles.activeTab]}
          onPress={() => setActiveTab('outgoing')}
        >
          <Text style={[styles.tabText, activeTab === 'outgoing' && styles.activeTabText]}>SALIENTES</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[styles.tab, activeTab === 'incoming' && styles.activeTab]}
          onPress={() => setActiveTab('incoming')}
        >
          <Text style={[styles.tabText, activeTab === 'incoming' && styles.activeTabText]}>ENTRANTES</Text>
        </TouchableOpacity>
      </View>

      <ScrollView contentContainerStyle={styles.scrollContent}>
        {activeTab === 'outgoing' ? (
          <View style={styles.section}>
            <Text style={styles.sectionTitle}>Módulo Intents Salientes</Text>

            <View style={styles.card}>
              <Text style={styles.cardTitle}>Panel 1: Llamador Misterioso</Text>
              <TextInput
                style={styles.input}
                placeholder="Número de teléfono"
                keyboardType="phone-pad"
                value={phoneNumber}
                onChangeText={setPhoneNumber}
              />
              <TouchableOpacity style={styles.button} onPress={handleDial}>
                <Text style={styles.buttonText}>INICIAR DIAL</Text>
              </TouchableOpacity>
            </View>

            <View style={styles.card}>
              <Text style={styles.cardTitle}>Panel 2: Foto Express</Text>
              <View style={styles.imagePlaceholder}>
                {capturedPhoto ? (
                  <Image source={{ uri: capturedPhoto }} style={styles.previewImage} />
                ) : (
                  <Text style={styles.placeholderText}>Sin miniatura</Text>
                )}
              </View>
              <TouchableOpacity style={[styles.button, styles.secondaryButton]} onPress={handleTakePhoto}>
                <Text style={styles.buttonText}>TOMAR FOTO</Text>
              </TouchableOpacity>
            </View>
          </View>
        ) : (
          <View style={styles.section}>
            <Text style={styles.sectionTitle}>Módulo Intents Entrantes</Text>

            <View style={[styles.card, styles.statusCard]}>
              <Text style={styles.statusLabel}>{statusMessage}</Text>
            </View>

            {receivedText && (
              <View style={styles.card}>
                <Text style={styles.cardTitle}>Texto Capturado</Text>
                <View style={styles.textDataContainer}>
                  <Text style={styles.receivedTextContent}>{receivedText}</Text>
                </View>
              </View>
            )}

            {receivedImage && (
              <View style={styles.card}>
                <Text style={styles.cardTitle}>Imagen Capturada</Text>
                <Image
                  source={{ uri: receivedImage }}
                  style={styles.fullImage}
                  resizeMode="contain"
                />
                <Text style={styles.binaryInfo}>[Archivo Binario detectado vía URI]</Text>
              </View>
            )}

            {!receivedText && !receivedImage && (
              <View style={styles.emptyContainer}>
                <Text style={styles.placeholderText}>No hay datos compartidos actualmente</Text>
              </View>
            )}
          </View>
        )}
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F5F5F5',
  },
  tabContainer: {
    flexDirection: 'row',
    backgroundColor: '#FFFFFF',
    elevation: 4,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
  },
  tab: {
    flex: 1,
    paddingVertical: 15,
    alignItems: 'center',
    borderBottomWidth: 3,
    borderBottomColor: 'transparent',
  },
  activeTab: {
    borderBottomColor: '#6200EE',
  },
  tabText: {
    fontSize: 14,
    fontWeight: '600',
    color: '#757575',
  },
  activeTabText: {
    color: '#6200EE',
  },
  scrollContent: {
    padding: 20,
  },
  section: {
    flex: 1,
  },
  sectionTitle: {
    fontSize: 22,
    fontWeight: 'bold',
    color: '#212121',
    marginBottom: 20,
  },
  card: {
    backgroundColor: '#FFFFFF',
    borderRadius: 12,
    padding: 16,
    marginBottom: 20,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.2,
    shadowRadius: 2,
  },
  cardTitle: {
    fontSize: 16,
    fontWeight: '600',
    color: '#424242',
    marginBottom: 12,
  },
  input: {
    borderWidth: 1,
    borderColor: '#E0E0E0',
    borderRadius: 8,
    padding: 12,
    fontSize: 16,
    marginBottom: 12,
  },
  button: {
    backgroundColor: '#6200EE',
    borderRadius: 8,
    padding: 14,
    alignItems: 'center',
  },
  secondaryButton: {
    backgroundColor: '#03DAC6',
  },
  buttonText: {
    color: '#FFFFFF',
    fontSize: 14,
    fontWeight: 'bold',
    letterSpacing: 1,
  },
  imagePlaceholder: {
    height: 150,
    backgroundColor: '#EEEEEE',
    borderRadius: 8,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 12,
    overflow: 'hidden',
  },
  previewImage: {
    width: '100%',
    height: '100%',
  },
  placeholderText: {
    color: '#9E9E9E',
    fontSize: 14,
  },
  statusCard: {
    backgroundColor: '#E8EAF6',
    alignItems: 'center',
  },
  statusLabel: {
    fontSize: 14,
    fontWeight: 'bold',
    color: '#3F51B5',
  },
  textDataContainer: {
    backgroundColor: '#F9F9F9',
    padding: 12,
    borderRadius: 8,
    borderLeftWidth: 4,
    borderLeftColor: '#6200EE',
  },
  receivedTextContent: {
    fontSize: 15,
    color: '#212121',
    lineHeight: 22,
  },
  fullImage: {
    width: '100%',
    height: 300,
    backgroundColor: '#000',
    borderRadius: 8,
  },
  binaryInfo: {
    marginTop: 8,
    fontSize: 12,
    color: '#757575',
    fontStyle: 'italic',
    textAlign: 'center',
  },
  emptyContainer: {
    alignItems: 'center',
    marginTop: 40,
  }
});

export default App;
