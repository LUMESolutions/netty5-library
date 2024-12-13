package de.dataflair.netty5;

/*
 * Copyright 2023-2024 netty5-api contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import de.dataflair.netty5.client.Netty5ClientPacketTransmitter;
import de.dataflair.netty5.common.codec.CodecBuffer;
import de.dataflair.netty5.common.packet.Packet;
import io.netty5.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor
public final class Netty5ClientChannel {

    @Setter
    private Identity identity;
    private final Channel channel;
    @Setter
    private Netty5ClientPacketTransmitter transmitter;

    public void sendPacket(@NotNull Packet packet) {
        channel.writeAndFlush(packet);
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Identity implements CodecBuffer.WriteReadStream {
        private String name;
        private UUID uuid;

        @Override
        public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
            codecBuffer.writeString(name)
                    .writeUniqueId(uuid);
        }

        @Override
        public void readBuffer(@NotNull CodecBuffer codecBuffer) {
            this.name = codecBuffer.readString();
            this.uuid = codecBuffer.readUniqueId();
        }
    }

    public record AuthType(Netty5ClientChannel clientChannel, Map<String, String> authProperty) {
    }

}
