# Bungecord-Sh
Allows you to run a shell script from the bungeecord console.

## Installation
Drop the .jar into your plugins folder, restart your server - a folder for this plugin will be created. Copy shell scripts into it.

## Usage
You can use this plugin to run shell scripts. Each script will be started in a separate thread. The script output will be send to the player who started the script.

## Commands and permissions
`/bsh [filename.sh] [arguments]` runs the script "filename.sh". Needs the permission **bsh.[filename.sh]** for every script (or **bsh.** for all, although this is not recommended)