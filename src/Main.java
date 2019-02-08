
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Main {
		
	public static void main(String[] arguments) throws Exception
	{
		FileEditor fileditor  = new FileEditor();
		fileditor.setgamestate("blank"); 
        //fileditor.clearfile("players.txt");
		//System.out.println(fileditor.readplayers());
		JDA api = new JDABuilder("NTM0ODg3NTY3NzQ1ODc1OTcw.DyAJcQ.Itx44iKqBZV6-Sq_WDmZqyXPv0s").build();
		api.awaitReady();
		api.addEventListener(new MyEventListener());
		
	}
		
		
	
}
/*okay what does this bot need to do?

Okay so I want to start a new round.
3 channels, hostbotcommands, botannouncements, playerbotcommands
4 roles, hosts, alive(capture), dead(deadcapture), limbo(limbocapture)
1 file containing one string. either "gameover", "setup", "distribution", "inprogress".

I say "!resetgame" in hbc.
Bot clears all role from the last game. 
Bot announces in ba that it's fielding players who want to play.

Players can then say !addme, or !removeme, into pbc
Hosts can say "!add @username" into hbc to force add.
Bot updates a list of current players in ba, reprompts for fielding players,
Changes all player roles to alive.

Once we're done with things, host in hbc says "!begindistribution"
Outputs ring randomized clothespins into ba, ring randomized cards into hbc.

Host distributes, then says "!beginround"
Bot reprints rules in ba, announces beginning of round and that it is fielding inputs from players on captures.

Players in pbc can then say "!captured @Username".
Bot adds mentioned player to limbo, prompts host in hbc that someone has been added, and prints a list of them.
Host in hbc can then say "!styx @Username" and if username has that role, moves that player to dead.
Host in hbc can also say "!save @Username" and if username has that role, moves that player to alive.

Once there is only one alive person and nobody in limbo, the game ends.
Game should also be able to be force ended with "!endgame" or just resetting with "!resetgame" -----
Winner winner chicken dinner role is given, and bot stops accepting commands.

*/