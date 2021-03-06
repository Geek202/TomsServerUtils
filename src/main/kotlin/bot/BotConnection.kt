package me.geek.tom.serverutils.bot

import com.mojang.authlib.GameProfile
import com.uchuhimo.konf.Config
import me.geek.tom.serverutils.GeneralSpec
import me.geek.tom.serverutils.discord.DiscordBotConnection
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

//private val LOGGER = LogManager.getLogger()

interface BotConnection {

    suspend fun connect(server: MinecraftServer)
    suspend fun disconnect()

    fun serverStarting(server: MinecraftServer)
    fun serverStarted(server: MinecraftServer)
    fun serverStopping(server: MinecraftServer)
    fun serverStopped(server: MinecraftServer)

    fun onBroadcast(text: Text)
    fun onChatMessage(user: GameProfile, headOverlay: Boolean, message: String)
    fun onPlayerLeave(player: ServerPlayerEntity)
    fun onPlayerJoin(player: ServerPlayerEntity)
    fun onPlayerAnnouncement(player: ServerPlayerEntity, message: Text, colour: Int)

}

class NoopBotConnection : BotConnection {
    override suspend fun connect(server: MinecraftServer) { }
    override suspend fun disconnect() { }
    override fun serverStarting(server: MinecraftServer) { }
    override fun serverStarted(server: MinecraftServer) { }
    override fun serverStopping(server: MinecraftServer) { }
    override fun serverStopped(server: MinecraftServer) { }
    override fun onBroadcast(text: Text) { }
    override fun onChatMessage(user: GameProfile, headOverlay: Boolean, message: String) { }
    override fun onPlayerLeave(player: ServerPlayerEntity) { }
    override fun onPlayerJoin(player: ServerPlayerEntity) { }
    override fun onPlayerAnnouncement(player: ServerPlayerEntity, message: Text, colour: Int) { }
}

enum class BotType {
    NONE, DISCORD
}

fun loadBot(config: Config): BotConnection {
    return when (config[GeneralSpec.mode]) {
        BotType.NONE -> NoopBotConnection()
        BotType.DISCORD -> DiscordBotConnection(config)
    }
}
