/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator;

import com.github.steveice10.mc.protocol.packet.ingame.server.*;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.*;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnGlobalEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.*;
import com.github.steveice10.mc.protocol.packet.login.server.LoginDisconnectPacket;
import com.github.steveice10.packetlib.packet.Packet;
import com.google.common.base.Preconditions;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.packet.*;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.bedrock.PEAnimateTranslator;
import org.dragonet.proxy.network.translator.bedrock.PECommandRequestTranslator;
import org.dragonet.proxy.network.translator.bedrock.PEInventoryTransactionTranslator;
import org.dragonet.proxy.network.translator.bedrock.PETextTranslator;
import org.dragonet.proxy.network.translator.bedrock.player.PEMovePlayerTranslator;
import org.dragonet.proxy.network.translator.bedrock.player.PEPlayerActionTranslator;
import org.dragonet.proxy.network.translator.java.*;
import org.dragonet.proxy.network.translator.java.entity.*;
import org.dragonet.proxy.network.translator.java.entity.spawn.PCSpawnGlobalEntityTranslator;
import org.dragonet.proxy.network.translator.java.entity.spawn.PCSpawnMobTranslator;
import org.dragonet.proxy.network.translator.java.entity.spawn.PCSpawnObjectTranslator;
import org.dragonet.proxy.network.translator.java.player.PCPlayerListDataTranslator;
import org.dragonet.proxy.network.translator.java.player.PCPlayerListEntryTranslator;
import org.dragonet.proxy.network.translator.java.player.PCPlayerPositionRotationTranslator;
import org.dragonet.proxy.network.translator.java.player.PCSpawnPlayerTranslator;
import org.dragonet.proxy.network.translator.java.world.*;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class PacketTranslatorRegistry<P> {
    public static final PacketTranslatorRegistry<BedrockPacket> BEDROCK_TO_JAVA = new PacketTranslatorRegistry<>();
    public static final PacketTranslatorRegistry<Packet> JAVA_TO_BEDROCK = new PacketTranslatorRegistry<>();

    static {
        JAVA_TO_BEDROCK.addTranslator(LoginDisconnectPacket.class, PCLoginDisconnectTranslator.INSTANCE)
            .addTranslator(ServerJoinGamePacket.class, PCJoinGameTranslator.INSTANCE)
            .addTranslator(ServerMultiBlockChangePacket.class, PCMultiBlockChangeTranslator.INSTANCE)
            .addTranslator(ServerDifficultyPacket.class, PCDifficultyTranslator.INSTANCE)
            .addTranslator(ServerTitlePacket.class, PCTitleTranslator.INSTANCE)
            .addTranslator(ServerEntityHeadLookPacket.class, PCEntityHeadlookTranslator.INSTANCE)
            .addTranslator(ServerEntityPositionPacket.class, PCEntityPositionTranslator.INSTANCE)
            .addTranslator(ServerEntityPositionRotationPacket.class, PCEntityPositionRotationTranslator.INSTANCE)
            .addTranslator(ServerEntityTeleportPacket.class, PCEntityTeleportTranslator.INSTANCE)
            .addTranslator(ServerEntityVelocityPacket.class, PCEntityVelocityTranslator.INSTANCE)
            .addTranslator(ServerUpdateTimePacket.class, PCUpdateTimeTranslator.INSTANCE)
            .addTranslator(ServerChatPacket.class, PCChatTranslator.INSTANCE)
            .addTranslator(ServerChunkDataPacket.class, PCChunkDataTranslator.INSTANCE)
            .addTranslator(ServerDisconnectPacket.class, PCDisconnectTranslator.INSTANCE)
            .addTranslator(ServerNotifyClientPacket.class, PCNotifyClientTranslator.INSTANCE)
            .addTranslator(ServerBossBarPacket.class, PCBossBarTranslator.INSTANCE)
            .addTranslator(ServerSpawnPlayerPacket.class, PCSpawnPlayerTranslator.INSTANCE)
            .addTranslator(ServerSpawnMobPacket.class, PCSpawnMobTranslator.INSTANCE)
            .addTranslator(ServerEntityEffectPacket.class, PCEntityEffectTranslator.INSTANCE)
            .addTranslator(ServerEntityRemoveEffectPacket.class, PCEntityRemoveEffectTranslator.INSTANCE)
            .addTranslator(ServerSetSlotPacket.class, PCSetSlotTranslator.INSTANCE)
            .addTranslator(ServerWindowItemsPacket.class, PCWindowItemsTranslator.INSTANCE)
            .addTranslator(ServerStatisticsPacket.class, PCStatisticsTranslator.INSTANCE)
            .addTranslator(ServerSpawnParticlePacket.class, PCSpawnParticleTranslator.INSTANCE)
            .addTranslator(ServerExplosionPacket.class, PCExplosionTranslator.INSTANCE)
            .addTranslator(ServerPlayerListEntryPacket.class, PCPlayerListEntryTranslator.INSTANCE)
            .addTranslator(ServerSpawnPositionPacket.class, PCSpawnPositionTranslator.INSTANCE)
            .addTranslator(ServerEntityMetadataPacket.class, PCEntityMetadataTranslator.INSTANCE)
            .addTranslator(ServerPlayerPositionRotationPacket.class, PCPlayerPositionRotationTranslator.INSTANCE)
            .addTranslator(ServerEntityPropertiesPacket.class, PCEntityPropertiesTranslator.INSTANCE)
            .addTranslator(ServerEntityDestroyPacket.class, PCEntityDestroyTranslator.INSTANCE)
            .addTranslator(ServerRespawnPacket.class, PCRespawnTranslator.INSTANCE)
            .addTranslator(ServerOpenWindowPacket.class, PCOpenWindowTranslator.INSTANCE)
            .addTranslator(ServerSpawnGlobalEntityPacket.class, PCSpawnGlobalEntityTranslator.INSTANCE)
            .addTranslator(ServerSpawnObjectPacket.class, PCSpawnObjectTranslator.INSTANCE)
            .addTranslator(ServerEntityRotationPacket.class, PCEntityRotationTranslator.INSTANCE)
            .addTranslator(ServerPlayerListDataPacket.class, PCPlayerListDataTranslator.INSTANCE);

        BEDROCK_TO_JAVA.addTranslator(TextPacket.class, PETextTranslator.INSTANCE)
            .addTranslator(AnimatePacket.class, PEAnimateTranslator.INSTANCE)
            .addTranslator(CommandRequestPacket.class, PECommandRequestTranslator.INSTANCE)
            .addTranslator(MovePlayerPacket.class, PEMovePlayerTranslator.INSTANCE)
            .addTranslator(PlayerActionPacket.class, PEPlayerActionTranslator.INSTANCE)
            .addTranslator(InventoryTransactionPacket.class, PEInventoryTransactionTranslator.INSTANCE);
    }

    private final Map<Class<?>, PacketTranslator<P>> translators = new HashMap<>();

    public void translate(ProxySession session, P packet) {
        Class<?> packetClass = packet.getClass();
        PacketTranslator<P> target = translators.get(packetClass);
        if (target == null) {
            //log.info("Unhandled packet received from remote: {}", packetClass.getSimpleName());
            return;
        }
        if (session.getDownstream() == null || session.getBedrockSession().isClosed()) {
            return;
        }
        log.trace("Translating packet: " + packetClass.getSimpleName());
        target.translate(session, packet);
    }

    @SuppressWarnings("unchecked")
    private <T extends P> PacketTranslatorRegistry<P> addTranslator(Class<T> clazz, PacketTranslator<T> packetTranslator) {
        Preconditions.checkNotNull(clazz, "clazz");
        Preconditions.checkNotNull(packetTranslator, "packetTranslator");
        translators.put(clazz, (PacketTranslator<P>) packetTranslator);
        return this;
    }
}