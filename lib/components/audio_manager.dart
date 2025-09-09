import 'dart:async';

import 'package:flame/components.dart';
import 'package:flame_audio/flame_audio.dart';

class AudioManager extends Component {
  bool musicEnabled = true;
  bool soundsEnabled = true;

  final List<String> _sounds = [
    'click',
    'collect',
    'explode1',
    'explode2',
    'fire',
    'hit',
    'laser',
    'start',
  ];

  @override
  FutureOr<void> onLoad() async {
    // initialize background music
    FlameAudio.bgm.initialize();

    // Pre-cache sounds so there's no initial delay when playing
    for (final s in _sounds) {
      final filename = '$s.ogg';
      // log what we're about to load to help diagnose duplicated paths
      // ignore: avoid_print
      print('[AudioManager] loading asset: $filename');
      try {
        await FlameAudio.audioCache.load(filename);
      } catch (e, st) {
        // ignore: avoid_print
        print('[AudioManager] failed to load $filename: $e\n$st');
      }
    }
    // Also pre-cache music
    try {
      // ignore: avoid_print
      print('[AudioManager] loading asset: music.ogg');
      await FlameAudio.audioCache.load('music.ogg');
    } catch (e, st) {
      // ignore: avoid_print
      print('[AudioManager] failed to load music.ogg: $e\n$st');
    }

    return super.onLoad();
  }

  void playMusic() {
    if (musicEnabled) {
      FlameAudio.bgm.play('music.ogg');
    }
  }

  void playSound(String sound) {
    if (!soundsEnabled) return;
    FlameAudio.play('$sound.ogg');
  }

  void toggleMusic() {
    musicEnabled = !musicEnabled;
    if (musicEnabled) {
      playMusic();
    } else {
      FlameAudio.bgm.stop();
    }
  }

  void toggleSounds() {
    soundsEnabled = !soundsEnabled;
  }
}
