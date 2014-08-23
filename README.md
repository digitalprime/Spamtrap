# SpamTrap

SpamTrap is a lightweight anti-spam plugin. If you run a server at some point you get hit by spammers and just problem players who it just ruin it for everyone else. This was originally a sub component of the dMod plugin. I decided to move it out and flesh it out as a plugin in its own right.

No anti-spam plugin will stop spammers from being creative but this uses a few simple techniques to keep there annoyances to a minimum without driving up your CPU usage.

# Features

 * Lightweight.
 * Simple and easy to configure.
 * Chat and command spam protection.
 * It just works!

# Installation

Just download and install in your `plugins` directory.

# Configuration

## General

 * `remove-whitespace`. Removes white space from chat.
 * `remove-repeating`. Removes repeating characters from the chat.
 * `remove-duplicate`. Removes duplicate player chat. An example is a spam of Hi! over multiple lines.

An example

    chat:
      remove-whitespace: true
      remove-repeating: true
      remove-duplicate: true

## Punishment
Punishment can be mute, kick or ban. Now ban is really a temporary ban and the cooldown is in seconds. This denoted by the cooldown setting. Mute simply mutes the player and kick does exactly that, kicks the player.

    punishment: ban
    cooldown: 120

## Recording/Logging Chat
To enable the logging or recording of player chat to a file you need to set the record chat to true. The date is used as the file name.

    chat:
      record: yes

## Messages
These are the messages relayed to the player when a punishment is handed out. Please note they are indented under messages: heading.

    kick: "Spamming!"
    mute: "You have been muted for spamming!"
    ban: "You have a temporary ban for spamming!"

## Player enter/leave message spam
Do reduce the amount of players entering and leaving messages use the following

    reduce-join-quit-messaging: yes

This is really only useful on busy servers. The default is no.

## SpamTrap version check
SpamTrap has the ability to check if it is up to date. It will not update itself but it will tell you if there is a more recent version.

    version-check: yes

## Permissions

 * `spamtrap.reload`. The default is set to operator.
 * `spamtrap.exempt`. Exemption from SpamTrap checks (this includes command protection). The default is set to operator.
