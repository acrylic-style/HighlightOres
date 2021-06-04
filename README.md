# Highlight ores
Highlights ores but... it depends on NMS.
This plugin doesn't work on main thread, so it should be lag-free.

## How it works
1. Creates entity (falling_block)
2. Set `glowing` and `NoGravity` to true
3. Send the SpawnEntity and EntityMetadata packet to the player
4. Remove the entity first, and send the block change packet to the player to render the block properly
