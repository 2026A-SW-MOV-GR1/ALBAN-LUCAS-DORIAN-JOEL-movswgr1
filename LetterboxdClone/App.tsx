import React, { useState, useCallback, memo } from 'react';
import {
  StyleSheet,
  View,
  Text,
  FlatList,
  Image,
  Pressable,
  SafeAreaView,
  StatusBar,
  Dimensions,
  Platform,
  useWindowDimensions,
} from 'react-native';

/**
 * Letterboxd Clone - Native UI Re-Engineering
 * React Native 0.86+ | Performance Optimized (60 FPS)
 * No External UI Libraries | Clean Code (SOLID)
 */

// --- 1. PALETA DE COLORES Y CONSTANTES ---
const COLORS = {
  bg: '#14181C',
  surface: '#2C3440',
  accentGreen: '#00E054',
  accentOrange: '#FF8000',
  accentBlue: '#40BCF4',
  textPrimary: '#FFFFFF',
  textSecondary: '#8B9AAB',
  border: '#445566',
};

// --- 2. MOCK DATA (Modelos de datos para Fase B) ---

const filmPosters = [
  'https://images.unsplash.com/photo-1536440136628-849c177e76a1',
  'https://images.unsplash.com/photo-1485846234645-a62644f84728',
  'https://images.unsplash.com/photo-1440404653325-ab127d49abc1',
  'https://images.unsplash.com/photo-1594909122845-11baa439b7bf',
  'https://images.unsplash.com/photo-1598897349489-3d17b473752e',
  'https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c',
  'https://images.unsplash.com/photo-1478720568477-152d9b164e26',
  'https://images.unsplash.com/photo-1542204111-97066ba8185c',
  'https://images.unsplash.com/photo-1535016120720-40c646bebbfc',
  'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba',
  'https://images.unsplash.com/photo-1509248961158-e54f6934749c',
  'https://images.unsplash.com/photo-1512149177596-f817c7ef5d4c',
  'https://images.unsplash.com/photo-1524712245354-2c4e5e7124c5',
  'https://images.unsplash.com/photo-1585647347384-2593bc35786b',
  'https://images.unsplash.com/photo-1505686994434-e3cc5abf1330',
  'https://images.unsplash.com/photo-1574267432553-4b4628081c31',
  'https://images.unsplash.com/photo-1595760780346-f972eb49709f',
  'https://images.unsplash.com/photo-1518676590629-3dcbd9c5a5c9',
];

const mockFilms = filmPosters.map((url, i) => ({
  id: `f-${i}`,
  title: `Film ${i + 1}`,
  posterUrl: `${url}?q=80&w=500&auto=format&fit=crop`,
}));

const mockReviews = [
  { id: 'r1', username: 'MacGuffin', avatarUrl: 'https://i.pravatar.cc/100?u=1', rating: 5, movieTitle: 'Dune: Part Two', reviewText: 'A visual masterpiece that redefines sci-fi cinema.', hasSpoiler: false },
  { id: 'r2', username: 'SpoilerLover', avatarUrl: 'https://i.pravatar.cc/100?u=2', rating: 4, movieTitle: 'Oppenheimer', reviewText: 'The sequence of the Trinity test was breathtaking, and the final scene where he talks to Einstein is the perfect emotional gut punch.', hasSpoiler: true },
  { id: 'r3', username: 'FilmCritic99', avatarUrl: 'https://i.pravatar.cc/100?u=3', rating: 3, movieTitle: 'Poor Things', reviewText: 'Lanthimos goes all in on the surrealism. Emma Stone is great, but the pacing felt a bit uneven.', hasSpoiler: false },
  { id: 'r4', username: 'DirectingFan', avatarUrl: 'https://i.pravatar.cc/100?u=4', rating: 5, movieTitle: 'The Zone of Interest', reviewText: 'The sound design is the real star here. Truly horrific.', hasSpoiler: false },
  { id: 'r5', username: 'MysteryFanatic', avatarUrl: 'https://i.pravatar.cc/100?u=5', rating: 4, movieTitle: 'Anatomy of a Fall', reviewText: 'The twist about the recording was expertly handled.', hasSpoiler: true },
  { id: 'r6', username: 'VintageSoul', avatarUrl: 'https://i.pravatar.cc/100?u=6', rating: 5, movieTitle: 'The Holdovers', reviewText: 'Paul Giamatti is at his absolute best.', hasSpoiler: false },
];

const mockJournals = [
  { id: 'j1', imageUrl: 'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?q=80&w=800', title: 'The Rise of Modern Noir', description: 'Exploring how directors are reinventing the shadows of the 40s.' },
  { id: 'j2', imageUrl: 'https://images.unsplash.com/photo-1478720568477-152d9b164e26?q=80&w=800', title: 'Why Aspect Ratios Matter', description: 'A deep dive into the psychological impact of 4:3 vs 2.39:1.' },
  { id: 'j3', imageUrl: 'https://images.unsplash.com/photo-1536440136628-849c177e76a1?q=80&w=800', title: 'Villeneuve Masterclass', description: 'How the Canadian director became the master of the modern sci-fi epic.' },
  { id: 'j4', imageUrl: 'https://images.unsplash.com/photo-1514306191717-452ec28c7814?q=80&w=800', title: 'Soundscapes of Horror', description: 'The terrifying power of off-screen sound in movies.' },
  { id: 'j5', imageUrl: 'https://images.unsplash.com/photo-1598897349489-3d17b473752e?q=80&w=800', title: 'The Art of the Long Take', description: 'Analyzing the technical brilliance behind famous extended shots.' },
  { id: 'j6', imageUrl: 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?q=80&w=800', title: 'Retro-Futurism', description: 'Looking at how Poor Things and Dune create worlds.' },
];

// --- 3. COMPONENTES RENDER_ITEM (Eficiencia de Listas) ---

const pressableStyle = ({ pressed }: { pressed: boolean }) => ({
  opacity: pressed ? 0.7 : 1,
  transform: [{ scale: pressed ? 0.98 : 1 }],
});

const FilmItem = memo(({ item, width }: { item: typeof mockFilms[0], width: number }) => (
  <Pressable style={pressableStyle}>
    <View style={[styles.filmContainer, { width: width - 4 }]}>
      <Image source={{ uri: item.posterUrl }} style={styles.filmPoster} resizeMode="cover" />
    </View>
  </Pressable>
));

const ReviewItem = memo(({ item }: { item: typeof mockReviews[0] }) => {
  const [isRevealed, setIsRevealed] = useState(false);

  return (
    <View style={styles.reviewCard}>
      <View style={styles.reviewHeader}>
        <Image source={{ uri: item.avatarUrl }} style={styles.avatar} />
        <View style={styles.reviewMeta}>
          <Text style={styles.username}>{item.username}</Text>
          <Text style={styles.movieTitleReview}>{item.movieTitle}</Text>
          <View style={styles.ratingRow}>
            {Array.from({ length: item.rating }).map((_, i) => (
              <Text key={i} style={styles.star}>★</Text>
            ))}
          </View>
        </View>
      </View>

      {/* Fase C: Propuesta de Mejora UX (Spoilers Interactivos) */}
      {item.hasSpoiler && !isRevealed ? (
        <Pressable
          style={({ pressed }) => [styles.spoilerContainer, pressableStyle({ pressed })]}
          onPress={() => setIsRevealed(true)}
        >
          <Text style={styles.spoilerWarning}>Review may contain spoilers.</Text>
          <Text style={styles.spoilerCallToAction}>Tap to reveal</Text>
        </Pressable>
      ) : (
        <Text style={styles.reviewText}>{item.reviewText}</Text>
      )}
    </View>
  );
});

const JournalItem = memo(({ item }: { item: typeof mockJournals[0] }) => (
  <Pressable style={pressableStyle}>
    <View style={styles.journalCard}>
      <Image source={{ uri: item.imageUrl }} style={styles.journalImage} />
      <View style={styles.journalContent}>
        <Text style={styles.journalTitle}>{item.title}</Text>
        <Text style={styles.journalDescription}>{item.description}</Text>
      </View>
    </View>
  </Pressable>
));

// --- 4. APP PRINCIPAL ---

export default function App() {
  const [activeTab, setActiveTab] = useState<'Films' | 'Reviews' | 'Journal'>('Films');
  const { width: windowWidth } = useWindowDimensions();
  const numColumns = windowWidth > 600 ? 5 : 3;

  const renderFilmItem = useCallback(({ item }: any) => (
    <FilmItem item={item} width={windowWidth / numColumns} />
  ), [windowWidth, numColumns]);

  const renderReviewItem = useCallback(({ item }: any) => <ReviewItem item={item} />, []);
  const renderJournalItem = useCallback(({ item }: any) => <JournalItem item={item} />, []);

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="light-content" backgroundColor={COLORS.bg} />

      {/* Header */}
      <View style={styles.header}>
        <Text style={styles.logo}>LETTERBOXD</Text>

        <View style={styles.tabBar}>
          {(['Films', 'Reviews', 'Journal'] as const).map((tab) => (
            <Pressable key={tab} onPress={() => setActiveTab(tab)} style={styles.tabButton}>
              <Text style={[styles.tabText, activeTab === tab && styles.tabTextActive]}>{tab}</Text>
              {activeTab === tab && <View style={styles.tabIndicator} />}
            </Pressable>
          ))}
        </View>
      </View>

      {/* Fase B: Implementación de Listas Eficientes */}
      <View style={{ flex: 1 }}>
        {activeTab === 'Films' && (
          <FlatList
            data={mockFilms}
            renderItem={renderFilmItem}
            keyExtractor={m => m.id}
            numColumns={numColumns}
            key={numColumns} // Solución técnica al error numColumns
            removeClippedSubviews={true}
            initialNumToRender={9}
            windowSize={5}
            maxToRenderPerBatch={9}
          />
        )}
        {activeTab === 'Reviews' && (
          <FlatList
            data={mockReviews}
            renderItem={renderReviewItem}
            keyExtractor={m => m.id}
            removeClippedSubviews={true}
            initialNumToRender={6}
          />
        )}
        {activeTab === 'Journal' && (
          <FlatList
            data={mockJournals}
            renderItem={renderJournalItem}
            keyExtractor={m => m.id}
            removeClippedSubviews={true}
            initialNumToRender={4}
          />
        )}
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: COLORS.bg },
  header: { backgroundColor: COLORS.bg, borderBottomWidth: 1, borderBottomColor: '#242b34' },
  logo: { color: COLORS.textPrimary, fontSize: 24, fontWeight: '900', textAlign: 'center', marginVertical: 15, letterSpacing: 2 },
  tabBar: { flexDirection: 'row', height: 45 },
  tabButton: { flex: 1, alignItems: 'center', justifyContent: 'center' },
  tabText: { color: COLORS.textSecondary, fontSize: 13, fontWeight: '700', textTransform: 'uppercase' },
  tabTextActive: { color: COLORS.textPrimary },
  tabIndicator: { position: 'absolute', bottom: 0, height: 3, width: '60%', backgroundColor: COLORS.accentGreen },
  filmContainer: { aspectRatio: 2/3, margin: 2, borderRadius: 4, overflow: 'hidden', backgroundColor: COLORS.surface },
  filmPoster: { flex: 1 },
  reviewCard: { padding: 16, borderBottomWidth: 1, borderBottomColor: COLORS.surface },
  reviewHeader: { flexDirection: 'row', marginBottom: 12 },
  avatar: { width: 36, height: 36, borderRadius: 18, marginRight: 12, backgroundColor: COLORS.surface },
  reviewMeta: { flex: 1 },
  username: { color: COLORS.textSecondary, fontSize: 12, fontWeight: '600' },
  movieTitleReview: { color: COLORS.accentBlue, fontSize: 16, fontWeight: '800' },
  ratingRow: { flexDirection: 'row', marginTop: 2 },
  star: { color: COLORS.accentGreen, fontSize: 10 },
  reviewText: { color: COLORS.textSecondary, fontSize: 14, lineHeight: 20 },
  spoilerContainer: { backgroundColor: '#2C3440', padding: 20, borderRadius: 8, borderWidth: 1, borderColor: COLORS.accentOrange, borderStyle: 'dashed', alignItems: 'center' },
  spoilerWarning: { color: COLORS.accentOrange, fontWeight: '700', fontSize: 13 },
  spoilerCallToAction: { color: COLORS.textSecondary, fontSize: 11, marginTop: 4 },
  journalCard: { backgroundColor: COLORS.surface, margin: 16, borderRadius: 12, overflow: 'hidden' },
  journalImage: { width: '100%', height: 180 },
  journalContent: { padding: 16 },
  journalTitle: { color: COLORS.textPrimary, fontSize: 18, fontWeight: '900' },
  journalDescription: { color: COLORS.textSecondary, fontSize: 13, marginTop: 4 },
});
