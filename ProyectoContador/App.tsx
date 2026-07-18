import React, { useState, useEffect } from 'react';
import {
  SafeAreaView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  AppState,
  AppStateStatus,
  StatusBar,
} from 'react-native';

const App = () => {
  const [count, setCount] = useState(0);
  const [appState, setAppState] = useState(AppState.currentState);

  useEffect(() => {
    const subscription = AppState.addEventListener('change', (nextAppState: AppStateStatus) => {
      let equivalentNativo = '';

      if (appState.match(/inactive|background/) && nextAppState === 'active') {
        equivalentNativo = 'onResume';
      } else if (appState === 'active' && nextAppState.match(/inactive|background/)) {
        equivalentNativo = 'onPause / onStop';
      }

      console.log(`[React Native AppState] Cambio detectado: ${nextAppState} (Equivalente Nativo: ${equivalentNativo})`);
      setAppState(nextAppState);
    });

    return () => {
      subscription.remove();
    };
  }, [appState]);

  const incrementCount = () => {
    setCount(count + 1);
  };

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" />
      <View style={styles.content}>
        <Text style={styles.title}>Taller Clase 07</Text>
        <Text style={styles.subtitle}>Batalla del Estado</Text>

        <View style={styles.counterContainer}>
          <Text style={styles.counterLabel}>Valor Actual:</Text>
          <Text style={styles.counterValue}>{count}</Text>
        </View>

        <TouchableOpacity
          style={styles.button}
          onPress={incrementCount}
          activeOpacity={0.7}
        >
          <Text style={styles.buttonText}>Incrementar +1</Text>
        </TouchableOpacity>

        <View style={styles.statusBox}>
          <Text style={styles.statusLabel}>Estado AppState:</Text>
          <Text style={[
            styles.statusValue,
            { color: appState === 'active' ? '#2ecc71' : '#e74c3c' }
          ]}>
            {appState.toUpperCase()}
          </Text>
        </View>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f6fa',
  },
  content: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#2f3640',
  },
  subtitle: {
    fontSize: 16,
    color: '#7f8c8d',
    marginBottom: 40,
  },
  counterContainer: {
    alignItems: 'center',
    marginBottom: 30,
    backgroundColor: '#ffffff',
    padding: 30,
    borderRadius: 20,
    width: '100%',
    elevation: 3,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
  },
  counterLabel: {
    fontSize: 18,
    color: '#7f8c8d',
  },
  counterValue: {
    fontSize: 80,
    fontWeight: '900',
    color: '#3498db',
  },
  button: {
    backgroundColor: '#3498db',
    paddingHorizontal: 40,
    paddingVertical: 15,
    borderRadius: 12,
    marginBottom: 40,
    width: '100%',
    alignItems: 'center',
  },
  buttonText: {
    color: '#ffffff',
    fontSize: 18,
    fontWeight: '600',
  },
  statusBox: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#ffffff',
    padding: 15,
    borderRadius: 10,
    borderWidth: 1,
    borderColor: '#dcdde1',
  },
  statusLabel: {
    fontSize: 14,
    color: '#7f8c8d',
    marginRight: 10,
  },
  statusValue: {
    fontSize: 14,
    fontWeight: 'bold',
  },
});

export default App;
