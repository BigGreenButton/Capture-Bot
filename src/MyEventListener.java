import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.*;
import net.dv8tion.jda.core.AccountType;


import javax.security.auth.login.LoginException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MyEventListener extends ListenerAdapter {


	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if (event.getAuthor().isBot()) return;
		// We don't want to respond to other bot accounts, including ourself
		FileEditor fileditor = new FileEditor();

		User author = event.getAuthor();
		Message message = event.getMessage();
		String msg = message.getContentRaw(); 
		MessageChannel channel = event.getChannel();
		Guild guild = event.getGuild();
		
		if(msg.equals("!1") && fileditor.getgamestate().equals("blank") && channel.getName().equals("host-bot-commands")) {
			fileditor.setgamestate("gameover");
		}
		else if(msg.equals("!2")&& fileditor.getgamestate().equals("blank") && channel.getName().equals("host-bot-commands")) {
			fileditor.setgamestate("setup");
		}
		else if(msg.equals("!3")&& fileditor.getgamestate().equals("blank") && channel.getName().equals("host-bot-commands")) {
			fileditor.setgamestate("distribution");
		}
		else if(msg.equals("!4")&& fileditor.getgamestate().equals("blank") && channel.getName().equals("host-bot-commands")) {
			fileditor.setgamestate("inprogress");
		}
		
		else if(fileditor.getgamestate().equals("blank")) {
			TextChannel hostsee = guild.getTextChannelsByName("host-bot-commands", true).get(0);
			hostsee.sendMessage("The gamestate has not been declared!\n"
					+ "The modding nor setup can be declared without it\n"
					+ "!1 sets it to gameover\n"
					+ "!2 sets it to setup\n"
					+ "!3 sets it to distribution\n"
					+ "!4 sets it to inprogress\n"
					+ "All of this is done quietly so don't worry about @-ing people").queue();
			return;
		}

		// () is an atomic getter
		// getContentDgetContentRawisplay() is a lazy getter which modifies the content for e.g. console view (strip discord formatting)
		else if (msg.equals("!ping"))
		{
			Role probation = guild.getRolesByName("botjail", true).get(0);
			if(message.getMember().getRoles().contains(probation))
				return;
			channel.sendMessage("Pong!").queue(); // Important to call .queue() on the RestAction returned by sendMessage(...)
		}

		else if (msg.equals("!hi")) {
			Role probation = guild.getRolesByName("botjail", true).get(0);
			if(message.getMember().getRoles().contains(probation))
				return;
			channel.sendMessage("Hi " + author.getName() + "!").queue();
		}
		

		else if (msg.equals("!help")) {
			Role probation = guild.getRolesByName("botjail", true).get(0);
			if(message.getMember().getRoles().contains(probation))
				return;
			Random rand = new Random();
			int n = rand.nextInt(25) + 50;
			if(n == 69) {
				channel.sendMessage("Each one teach one: https://pastebin.com/Z6kxA3sF").queue();
			}
			else {
				if(channel.getName().equals("player-bot-commands")) {
					if(fileditor.getgamestate().equals("setup")) {
						channel.sendMessage("Hello " + author.getName() + ". I'm a bot made for playing capture!\n"
								+ "Here's a simple list of your current avaiable commands during this setup time:\n"
								+ "**!ping** : Pong!\n"
								+ "**!hi** : Say hello!\n"
								+ "**!rules** : Prints the rules!\n"
								+ "**!listplayers** : Displays list of all current players.\n"
								+ "**!addme** : Adds you to the list of players ready for the next game.\n"
								+ "**!removeme** : Removes you from that list.\n"
								+ "").queue();
					}
					else if(fileditor.getgamestate().equals("distribution")) {
						channel.sendMessage("Hello " + author.getName() + ". I'm a bot made for playing capture!\n"
								+ "Here's a simple list of your current avaiable commands during this distribution time:\n"
								+ "**!ping** : Pong!\n"
								+ "**!hi** : Say hello!\n"
								+ "**!rules** : Prints the rules!\n"
								+ "**!listplayers** : Displays list of all current players.\n"
								+ "").queue();
					}
					else if(fileditor.getgamestate().equals("inprogress")) { 
						channel.sendMessage("Hello " + author.getName() + ". I'm a bot made for playing capture!\n"
								+ "Here's a simple list of your current avaiable commands during game time:\n"
								+ "**!ping** : Pong!\n"
								+ "**!hi** : Say hello!\n"
								+ "**!rules** : Prints the rules!\n"
								+ "**!listplayers** : Displays list of all **alive** players.\n"
								+ "**!capture @Username** : Report a capture! It will be confirmed by the host later, but presumed accurate").queue();
					}
					else if(fileditor.getgamestate().equals("gameover")) {
						channel.sendMessage("Hello " + author.getName() + ". I'm a bot made for playing capture!\n"
								+ "Here's a simple list of your current avaiable commands during this time with no active game:\n"
								+ "**!ping** : Pong!\n"
								+ "**!hi** : Say hello!\n"
								+ "**!rules** : Prints the rules!\n"
								+ "").queue();
					}
				}
				
				else if(channel.getName().equals("host-bot-commands")) {
					channel.sendMessage("Hello host. I'm a bot made for playing capture!\n"
							+ "If you would like a basic overview of how this bot works,\n"
							+ "click here and read the readme: https://github.com/BigGreenButton/Capture-Bot").queue();
				}
			}
			
			
			
		}
		
		else if (msg.startsWith("!rules")) {
			Role probation = guild.getRolesByName("botjail", true).get(0);
			if(!channel.getName().equals("player-bot-commands") || !channel.getName().equals("player-bot-commands") 
					|| message.getMember().getRoles().contains(probation)) {
				return;
			}
			
			channel.sendMessage("https://pastebin.com/5ExajAUU").queue();
		}

		//------------------------------------------------------------------------------------------------
		else if (msg.startsWith("!endgame")) {       	
			Role probation = guild.getRolesByName("botjail", true).get(0);
			if(message.getMember().getRoles().contains(probation)) {
				return;
			}
			if(!channel.getName().equals("host-bot-commands")) {
				channel.sendMessage("You are not authorized to use this command.").queue();
				return;
			}        	
			if(fileditor.getgamestate().equals("gameover")) {
				channel.sendMessage("There is no current game. Try !resetgame instead.").queue();
				return;
			}

			else {

				List<TextChannel> channels = guild.getTextChannelsByName("bot-announcements", true);
				Role alive = guild.getRolesByName("capture", true).get(0);
				Role dead = guild.getRolesByName("deadcapture", true).get(0);
				Role limbo = guild.getRolesByName("limbocapture", true).get(0);
				Role hosts = guild.getRolesByName("capture host", true).get(0);
				channels.get(0).sendMessage(alive.getAsMention() + ", " + dead.getAsMention() + ", " + limbo.getAsMention() +"\n" //TODO? add who won?
						+ "The game has ended! Thanks for playing!\nPlease return your items to " + guild.getMembersWithRoles(hosts).get(0).getAsMention()
						+ " as soon as possible!").queue();
				fileditor.setgamestate("gameover");

				removeAllGameRoles(guild);

			}


		}
		
		else if (msg.startsWith("!clearroles")) {
			Role probation = guild.getRolesByName("botjail", true).get(0);
			if(message.getMember().getRoles().contains(probation)) {
				return;
			}
			if(!channel.getName().equals("host-bot-commands")) { 
				channel.sendMessage("You are not authorized to use this command.").queue();
				return;
			}
			removeAllGameRoles(guild);
		}
		
		else if (msg.startsWith("!resetgame")) {
			Role probation = guild.getRolesByName("botjail", true).get(0);
			if(message.getMember().getRoles().contains(probation)) {
				return;
			}
			if(!channel.getName().equals("host-bot-commands")) { 
				channel.sendMessage("You are not authorized to use this command.").queue();
				return;
			}
			if(fileditor.getgamestate().equals("gameover")) {
				fileditor.setgamestate("setup");
				TextChannel announce = guild.getTextChannelsByName("bot-announcements", true).get(0);
				announce.sendMessage("@everyone\nWe are setting up the new game!\nType \"!addme\" into #player-bot-commands to join!"
						+ "\n(You can also type \"!removeme\" to remove your name from the list and \"!listplayers\" to see "
						+ "a list of the current players.) [also try \"!help\"]").queue();
				
				
				//below removes all related roles.
				//also done by !endgame.
				
				/*Role alive = guild.getRolesByName("capture", true).get(0);
				Role dead = guild.getRolesByName("deadcapture", true).get(0);
				Role limbo = guild.getRolesByName("limbocapture", true).get(0);
				List<Member> members = guild.getMembers();
				
				for (Member member : members) {
					if(member.getRoles().contains(alive))
						guild.getController().removeRolesFromMember(member, alive).complete();
					if(member.getRoles().contains(dead))
						guild.getController().removeRolesFromMember(member, dead).complete();
					if(member.getRoles().contains(limbo))
						guild.getController().removeRolesFromMember(member, limbo).complete();
				}*/
				
			}
			else {
				channel.sendMessage("You cannot reset this game yet. Please end it with !endgame.").queue();
			}

		}
		
		//first non host only command
		else if (msg.startsWith("!addme")) {
			Role probation = guild.getRolesByName("botjail", true).get(0);
			if(!channel.getName().equals("player-bot-commands") || !fileditor.getgamestate().equals("setup") || message.getMember().getRoles().contains(probation)) { 
				return;
			}
			

			List<Role> alive = guild.getRolesByName("capture", true);
			guild.getController().addRolesToMember(event.getMember(), alive).complete();

			TextChannel announce = guild.getTextChannelsByName("bot-announcements", true).get(0);      	
			announce.sendMessage(event.getMember().getEffectiveName() + " joined the next game!").queue();
		}

		else if (msg.startsWith("!removeme")) {
			Role probation = guild.getRolesByName("botjail", true).get(0);
			if(!channel.getName().equals("player-bot-commands") || !fileditor.getgamestate().equals("setup") || message.getMember().getRoles().contains(probation)) { 
				return;
			}
			List<Role> alive = guild.getRolesByName("capture", true);
			guild.getController().removeRolesFromMember(event.getMember(), alive).complete();

			TextChannel announce = guild.getTextChannelsByName("bot-announcements", true).get(0);
			announce.sendMessage(event.getMember().getEffectiveName() + " left the next game!").queue();
		}

		else if (msg.startsWith("!listplayers")) { //can be done at anytime except for endgame
			Role probation = guild.getRolesByName("botjail", true).get(0);
			if(!channel.getName().equals("player-bot-commands") || fileditor.getgamestate().equals("gameover") || message.getMember().getRoles().contains(probation)) { 
				return;
			}

			List<Role> alive = guild.getRolesByName("capture", true);
			List<Member> players = guild.getMembersWithRoles(alive);

			String output = "The current players are:\n";
			for (Member member : players) {
				output = output + member.getEffectiveName() + " - ";
			}
			channel.sendMessage(output).queue();
		}

		else if (msg.startsWith("!begindistribution")) {
			Role probation = guild.getRolesByName("botjail", true).get(0);
			if(message.getMember().getRoles().contains(probation)) {
				return;
			}
			if(!channel.getName().equals("host-bot-commands")) {
				channel.sendMessage("You are not authorized to use this command.").queue();
				return;
			}        	
			if(!fileditor.getgamestate().equals("setup")) {
				channel.sendMessage("You aren't in the right gamestate for this command.").queue();
				return;
			}

			
			TextChannel announce = guild.getTextChannelsByName("bot-announcements", true).get(0);
			TextChannel hostsee = guild.getTextChannelsByName("host-bot-commands", true).get(0);

			Role alive = guild.getRolesByName("capture", true).get(0);
			announce.sendMessage(alive.getAsMention() + ", the game will be starting soon!").queue();

			List<String> names = new ArrayList<String>();
			for(Member member : guild.getMembersWithRoles(alive)) {
				names.add(member.getEffectiveName());
			}
			
			if(names.size() < 3) { 
				channel.sendMessage("Please only use this command if at least 3 people are playing.").queue();
				return;
			}
			fileditor.setgamestate("distribution"); //only changes stuff after checking
			
			Collections.shuffle(names);

			List<Member> players = new ArrayList<Member>();
			for(String name : names) {
				players.add(guild.getMembersByName(name, true).get(0));
			}
			
			for (int i = 0; i < players.size(); i++) {
				if(i != players.size()-1) //normal
					announce.sendMessage("**" + players.get(i).getEffectiveName() + "** has "
							+ "" + players.get(i+1).getEffectiveName() + "'s clothespin.").queue();
				else //if last one
					announce.sendMessage("**" + players.get(i).getEffectiveName() + "** has "
							+ "" + players.get(0).getEffectiveName() + "'s clothespin.").queue();
			}
			announce.sendMessage("Please report to the host IRL to receive your items.").queue();

			//TODO TEST BELOW TO SEE IF THE RANDOMIZIATION OF THE OFFSET WORKS

			List<Member> playershift = new ArrayList<Member>();
			for(String name : names) {
				playershift.add(guild.getMembersByName(name, true).get(0));
			}
			
			Random rand = new Random();
			int n = 2; //n is the offset value for the ring.
			if(playershift.size() > 3)
				n = rand.nextInt(playershift.size()-1) + 2;
			//it's size-1 because if you offset by the size there it loops back to when you started.

			for (int i = 0; i < n; i++) { //offsets by n.
				playershift.add(0, playershift.get(playershift.size()-1));
				playershift.remove(playershift.size()-1);
			}

			for (int i = 0; i < playershift.size(); i++) {
				if(i != playershift.size()-1) 
					hostsee.sendMessage("**" + playershift.get(i).getEffectiveName() + "** has "
							+ "" + players.get(i).getEffectiveName() + "'s second clothespin.").queue();
				else
					hostsee.sendMessage("**" + playershift.get(i).getEffectiveName() + "** has "
							+ "" + players.get(i).getEffectiveName() + "'s second clothespin.").queue();
			}
		}
		
		else if (msg.startsWith("!beginround")) {
			Role probation = guild.getRolesByName("botjail", true).get(0);
			if(message.getMember().getRoles().contains(probation)) {
				return;
			}
			if(!channel.getName().equals("host-bot-commands")) {
				channel.sendMessage("You are not authorized to use this command.").queue();
				return;
			}        	
			if(!fileditor.getgamestate().equals("distribution")) {
				channel.sendMessage("You aren't in the right gamestate for this command.").queue();
				return;
			}
			
			fileditor.setgamestate("inprogress");
			TextChannel announce = guild.getTextChannelsByName("bot-announcements", true).get(0);
			TextChannel pbc = guild.getTextChannelsByName("player-bot-commands", true).get(0);
			Role alive = guild.getRolesByName("capture", true).get(0);
			
			announce.sendMessage(alive.getAsMention() + ", The game has begun!\nYou can now type \"**!capture @Username**\" in " + pbc.getAsMention() + " to log captures!").queue();
			
		}
		
		else if(msg.startsWith("!capture")) {
			Role alive = guild.getRolesByName("capture", true).get(0);
			Role probation = guild.getRolesByName("botjail", true).get(0);
			
			Boolean isbot = false;
			List<User> wait = message.getMentionedUsers();
			for(User user : wait) {
				if(user.isBot())
					isbot = true;
			}
			
			if(!channel.getName().equals("player-bot-commands") || !fileditor.getgamestate().equals("inprogress") 
					|| !message.getMember().getRoles().contains(alive) || message.getMember().getRoles().contains(probation)
					|| isbot) {
				return;
			} 
			
			if(message.getMentionedMembers().contains(message.getMember())) {
				channel.sendMessage("Why the hel- Look c'mon man why even try it.\nDid you think I wouldn't anticipate this?\n"
						+ "I should put you in botjail lmao.").queue();
				return;
			}
			
			boolean isplaying = false;
			//check if the captee is playing, loop over mentioned players. send msg "one or more are not playing, plz try again"
			for (Member member : message.getMentionedMembers()) {
				isplaying = (guild.getMembersWithRoles(alive).contains(member));
			}
			if(!isplaying) {
				channel.sendMessage("One or more of your mentioned players are not alive or playing the game.\nPlease try again.").queue();
				return;
			}
			
			TextChannel announce = guild.getTextChannelsByName("bot-announcements", true).get(0);
			TextChannel hbc = guild.getTextChannelsByName("host-bot-commands", true).get(0);
			Role limbo = guild.getRolesByName("limbocapture", true).get(0);
			Role hosts = guild.getRolesByName("capture host", true).get(0);
			Role winner = guild.getRolesByName("winner winner chicken dinner", true).get(0);
			Member thehost = guild.getMembersWithRoles(hosts).get(0);
			
			for(Member member : message.getMentionedMembers()) {
				announce.sendMessage(member.getAsMention() + " CAPTURED by " + author.getAsMention()).queue();
				announce.sendMessage(member.getAsMention() + " please relinquish your items to " + author.getAsMention() + " at the nearest opportunity.").queue();
				announce.sendMessage("---").queue();
				changeMemberGameRolesTo(guild, member, limbo);
				channel.sendMessage(member.getEffectiveName() + ", captured!").queue();
				hbc.sendMessage(thehost.getAsMention() + ", " + member.getEffectiveName() + " has been alleged as captured. Please !styx or !save them asap.").queue();
			}
			
		}
		
		else if (msg.startsWith("!styx")) {
			Role probation = guild.getRolesByName("botjail", true).get(0);
			if(message.getMember().getRoles().contains(probation)) {
				return;
			}
			if(!channel.getName().equals("host-bot-commands")) {
				return;
			}        	
			if(!fileditor.getgamestate().equals("inprogress")) {
				return;
			}
			
			TextChannel announce = guild.getTextChannelsByName("bot-announcements", true).get(0);
			TextChannel hbc = guild.getTextChannelsByName("host-bot-commands", true).get(0);
			Role alive = guild.getRolesByName("capture", true).get(0);
			//probation declared above
			Role dead = guild.getRolesByName("deadcapture", true).get(0);
			Role limbo = guild.getRolesByName("limbocapture", true).get(0);
			Role hosts = guild.getRolesByName("capture host", true).get(0);
			Role winner = guild.getRolesByName("winner winner chicken dinner", true).get(0);
			
			List<Member> awaiters = guild.getMembersWithRoles(limbo);
			List<Member> mentioned = message.getMentionedMembers();
			boolean winnerexists = false;
			
			for(Member member : mentioned) {
				if(!awaiters.contains(member)) {
					hbc.sendMessage("This person is not in limbo.").queue();
				}
				else {
					changeMemberGameRolesTo(guild, member, dead);
					hbc.sendMessage(member.getEffectiveName() + " has been succesfully styxed!").queue();
					winnerexists = (guild.getMembersWithRoles(alive).size() == 1);
				}
			}
			
			if(winnerexists) {
				announce.sendMessage(alive.getAsMention() + ", " + dead.getAsMention() +", the round has ended!\n"
						+ "The winner of this round is " + guild.getMembersWithRoles(alive).get(0).getAsMention() + ".").queue();
				hbc.sendMessage(guild.getMembersWithRoles(hosts).get(0).getAsMention() + ", The game has been ended automatically.").queue();
				fileditor.setgamestate("gameover");
				guild.getController().addRolesToMember(guild.getMembersWithRoles(alive).get(0), winner).complete();			
			}
		}
		
		else if (msg.startsWith("!save")) {
			Role probation = guild.getRolesByName("botjail", true).get(0);
			if(message.getMember().getRoles().contains(probation)) {
				return;
			}
			if(!channel.getName().equals("host-bot-commands")) {
				return;
			}        	
			if(!fileditor.getgamestate().equals("inprogress")) {
				return;
			}
			
			TextChannel announce = guild.getTextChannelsByName("bot-announcements", true).get(0);
			TextChannel hbc = guild.getTextChannelsByName("host-bot-commands", true).get(0);
			Role alive = guild.getRolesByName("capture", true).get(0);
			//probation declared above
			Role limbo = guild.getRolesByName("limbocapture", true).get(0);
			Role hosts = guild.getRolesByName("capture host", true).get(0);
			Role winner = guild.getRolesByName("winner winner chicken dinner", true).get(0);
			
			List<Member> awaiters = guild.getMembersWithRoles(limbo);
			List<Member> mentioned = message.getMentionedMembers();
			
			for(Member member : mentioned) {
				if(!awaiters.contains(member)) {
					hbc.sendMessage("This person is not in limbo.").queue();
				}
				else {
					changeMemberGameRolesTo(guild, member, alive);
					hbc.sendMessage(member.getEffectiveName() + " has been succesfully saved!").queue();
					announce.sendMessage(alive.getAsMention() + ", the capture of " + member.getEffectiveName() + " has been deemed faulty.\nThey have been saved and "
							+ "their captor should send the host, " + guild.getMembersWithRoles(hosts).get(0).getEffectiveName() + ", a dm immediately.").queue();
				}
				
			}
		}
		


	}
	
	public static void removeAllGameRoles(Guild guild) {
		//the goal is to get it recognize each role then only take the necessary oneas
		List<Member> allpeople = guild.getMembers();
		for(Member member : allpeople) {
			List<Role> roles = member.getRoles();
			for(Role role : roles) {
				if(role.getName().equals("capture") || role.getName().equals("deadcapture") || role.getName().equals("limbocapture")) {
					roles.remove(role);
				}
			}
			guild.getController().modifyMemberRoles(member, roles);
		}
	}
	public static void changeMemberGameRolesTo(Guild guild, Member target, Role change) {
		//the goal is to get it recognize each role then only take the necessary oneas
		List<Role> roles = target.getRoles();
		for(Role role : roles) {
			if(role.getName().equals("capture") || role.getName().equals("deadcapture") || role.getName().equals("limbocapture")) {
				roles.remove(role);
			}
		}
		roles.add(change);
		guild.getController().modifyMemberRoles(target, roles);
	}



}

