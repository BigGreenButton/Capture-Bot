# CaptureBot
A bot for organizing and playing a game of capture through a discord server.

Uses https://github.com/DV8FromTheWorld/JDA!

Hello! Welcome to capture bot!
It's a bot designed to,,,, play capture. What is that?
Well it's similar to assassin. Rules here: https://pastebin.com/5ExajAUU

This bot works by manipulating roles in accordance with commands and a gamestate.
It does require:
 - a txt file in its src named "gamestate.txt"
 - a channel named "host-bot-commands" (restricted reading and writing to HOSTS)
 - a channel named "player-bot-commands" (restricted writing to the BOT)
 - a channel named "bot-announcements"
 - a role named capture
 - a role named deadcapture
 - a role named limbocapture
 - a role named capture host
 - a role named winner winner chicken dinner
 - a role named botjail
 - a role named capturebot (not a part of the code, but useful for channel management)

It is designed to be open to host manipulation if the bot does not handle things correctly.
Say for example you capture someone who shouldn't have, if the command preventing this fails, then you can just change their role.
While the bot messages will seem a little out of whack, everything will still function. 
If the bot breaks because you changed a role, I failed. 
(the one exception being where there are only two people left. changing the role from alive to dead will not induce a winner message. in this case however you can just change them back to alive and capture them through the bot to induce it)

Game states can be one of four:
 - gameover (when there is no game nor setup currently in progress)
 - setup (when people are deciding whether or not they would like to join the next round)
 - distribution (when the host [likely you] is handing out cards and clothespins physically)
 - inprogress (when the game is, well, happening)

As a host you will need to flip through these states with: 
 - !endgame (will clear all related roles)
 - !resetgame (will clear all related roles)
 - !begindistribution 
 - !beginround (listed in the order of normal game)
 
!endgame can be called at anytime, but the others can only be called in order.
IF YOU ARE INITIALIZING THE BOT ON A SERVER:
It begins in the "gameover" so start with "!resetgame".


The other host-only bot commands are !styx and !save.
When someone is !capture(d), they are moved from capture to capturelimbo.
Then you will be prompted to !styx (confirm their demise) or !save (return them to the living)
You will make this choice based on the photo proof you receive.

Other than that you won't have to use the bot commands, just normal modding things.
Moving roles around, handing items out, and oh wait one thing.
There is a role called "botjail" which basically acts as bot probation.
If someone is abusing the bot, you can put them in botjail to restrict their ability to use commands (i.e do things without your ability to change roles)
DO WATCH OUT, THERE ARE NO PROBATION NOR ROLE RESTRICTIONS ON HOST BOT COMMANDS.
THE RESTRICTION MUST BE SET UP IN THE HOST BOT COMMAND CHANNEL.
DO NOT ALLOW NON HOSTS TO READ OR WRITE IN THAT CHANNEL.
IF YOU FAIL THAT, NOTHING WILL SAVE YOU.

Also, if the the randomization when distributing things is flawed, god dammit.
I spent a lot of time on it and im hoping it doesn't break.
Send me a message and I'll fix it but hopefully it will not matter.

Past that, I wish you luck on your hosting endeavors.
Regards,
Aylea
