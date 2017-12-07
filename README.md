# DiscLoader
A High Level Java wrapper for the Discord API written in 
Created to replace the method daisy chaining confusion of other Java libraries for the Discord API.

[Documentation](http://docs.discloader.io)

[Discord Guild](https://discord.gg/7zfDPhE)

[Programming Help](http://discord.me/cm)

[Example](https://github.com/R3alCl0ud/DiscLoader/blob/master/example/com/example/Example.java)

[Contribution guildlines](https://github.com/R3alCl0ud/DiscLoader/blob/master/CONTRIBUTING.md)

```xml
<dependencies>
    <dependency>
        <groupId>io.discloader</groupId>
        <artifactId>discloader</artifactId>
        <version>0.2.2</version>
    </dependency>
</dependencies>
<repositories>
    <repository>
        <id>jcenter</id>
        <name>jcenter-bintray</name>
        <url>http://jcenter.bintray.com</url>
    </repository>
</repositories>
```


# Done
- Connecting to the gateway via websocket
- Change username/nickname/guild name
- Change avatar/guild icon
- Sending messages
- Sending embeds
- Sending Files
- Editing messages and embeds
- Deleting messages
- Command System
- Voice Connections
- Gateway reconnecting and resuming
- Added Channel Categories


# Todo
- Finish constants registry.
- Finish packet reading.
- Parse EmbedJSONs to a MessageEmbed type.
- Add methods for editing a Channel's data.
- add better documentation

### Command Line Arguments

| arg | description | default |
|-----|--------------|---------|
| `nogui` | if DiscLoader is executed with nogui as an argument the GUI window doesn't show | loads GUI |
| `-t=<token>` | The api token to use to authenticate | `""` |
| `-p=<prefix>` | The prefix to use for commands | `"//"` |
| `-s=<shard>:<shards>` | The info for sharding | `"0:1"` |

### Options file

| property | description | default |
|----------|-------------|---------|
| `useWindow` | whether or not to use the included GUI | `true` |
| `token` | The api token to use for authentication | `""` |
| `prefix` | The prefix to look for at the start of a message | `//` |
| `shard` | The shard number | `0` |
| `shards` | The number of shards | `1` |
