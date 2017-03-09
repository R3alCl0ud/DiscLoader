# DiscLoader
Simple Discord API abstractions for Java.
Created to replace the method daisy chaining confusion of other Java libraries for the Discord API.

[Documentation](https://r3alcl0ud.github.io/DiscLoader/)

[Example](null)

# Done
- Connecting to the gateway via websocket
- Change username/nickname/guild name
- Change avatar/guild icon
- Sending messages
- Sending embeds
- Editing messages and embeds
- Deleting messages
- Command System
- A Voice connection system

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
| `-t` | The api token to use to authenticate | `""` |
| `-p` | The prefix to use for commands | `"//"` |

### Options file

| property | description | default |
|----------|-------------|---------|
| `useWindow` | whether or not to use the included GUI | `true` |
| `token` | The api token to use for authentication | `""` |
| `prefix` | The prefix to look for at the start of a message | `//` |
| `shard` | The shard number | `0` |
| `shards` | The number of shards | `1` |
