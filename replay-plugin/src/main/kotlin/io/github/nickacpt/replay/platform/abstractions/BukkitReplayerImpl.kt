package io.github.nickacpt.replay.platform.abstractions

import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.behaviours.replay.abstractions.ReplayPlatform
import io.github.nickacpt.behaviours.replay.playback.Replayer

class BukkitReplayerImpl<NativeItemStack,
        NativeViewer,
        NativeWorld,
        Platform : ReplayPlatform<NativeItemStack, NativeViewer, NativeWorld>,
        System : ReplaySystem<NativeItemStack, NativeViewer, NativeWorld, Platform>> :
    Replayer<NativeItemStack, NativeViewer, NativeWorld, Platform, System>