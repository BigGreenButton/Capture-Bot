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



		// () is an atomic getter
		// getContentDgetContentRawisplay() is a lazy getter which modifies the content for e.g. console view (strip discord formatting)
		if (msg.equals("!ping"))
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
								+ "**!listplayers** : Displays list of all current players.\n"
								+ "").queue();
					}
					else if(fileditor.getgamestate().equals("inprogress")) { 
						channel.sendMessage("Hello " + author.getName() + ". I'm a bot made for playing capture!\n"
								+ "Here's a simple list of your current avaiable commands during game time:\n"
								+ "**!ping** : Pong!\n"
								+ "**!hi** : Say hello!\n"
								+ "**!listplayers** : Displays list of all **alive** players.\n"
								+ "**!capture @Username** : Report a capture! It will be confirmed by the host later, but presumed accurate").queue();
					}
					else if(fileditor.getgamestate().equals("gameover")) {
						channel.sendMessage("Hello " + author.getName() + ". I'm a bot made for playing capture!\n"
								+ "Here's a simple list of your current avaiable commands during this time with no active game:\n"
								+ "**!ping** : Pong!\n"
								+ "**!hi** : Say hello!\n"
								+ "").queue();
					}
				}
				
				else if(channel.getName().equals("host-bot-commands")) {
					channel.sendMessage("Hello host. I'm a bot made for playing capture!\n"
							+ "If you would like a basic overview of how this bot works, click here: https://pastebin.com/feqsaZKY").queue();
				}
			}
			
			
			
		}
		
		else if (msg.startsWith("!rules!")) {
			Role probation = guild.getRolesByName("botjail", true).get(0);
			if(!channel.getName().equals("player-bot-commands") || !channel.getName().equals("player-bot-commands") 
					|| message.getMember().getRoles().contains(probation)) {
				return;
			}
			
			channel.sendMessage("https://pastebin.com/5ExajAUU").queue();
		}

		//------------------------------------------------------------------------------------------------
		else if (msg.startsWith("!endgame")) {       	
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



				List<Member> members = guild.getMembers();

				//Note for this loop. You cannot remove two roles from the same member in one event listener cycle.
				//Something about cache updates. Anyway, it doesnt matter because NOBODY should have two roles at once! hint hint.
				for (Member member : members) {
					if(member.getRoles().contains(alive))
						guild.getController().removeRolesFromMember(member, alive).complete();
					if(member.getRoles().contains(dead))
						guild.getController().removeRolesFromMember(member, dead).complete();
					if(member.getRoles().contains(limbo))
						guild.getController().removeRolesFromMember(member, limbo).complete();
				}

			}


		}

		else if (msg.startsWith("!resetgame")) {
			if(!channel.getName().equals("host-bot-commands")) { 
				channel.sendMessage("You are not authorized to use this command.").queue();
				return;
			}
			if(fileditor.getgamestate().equals("gameover")) {
				fileditor.setgamestate("setup");
				TextChannel announce = guild.getTextChannelsByName("bot-announcements", true).get(0);
				announce.sendMessage("@everyone\nWe are setting up the new game!\nType \"!addme\" into #player-bot-commands to join!"
						+ "\n(You can also type \"!removeme\" to remove your name from the list and \"!listplayers\" to see "
						+ "a list of the current players.)").queue();
				
				Role alive = guild.getRolesByName("capture", true).get(0);
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
				}
				/*
				List<Role> alive = guild.getRolesByName("capture", true);
				for(Member member : guild.getMembers()) {
					guild.getController().addRolesToMember(member, alive).complete();
				}*/ //only for testing
				
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
			announce.sendMessage(event.getAuthor().getName() + " joined the next game!").queue();
		}

		else if (msg.startsWith("!removeme")) {
			Role probation = guild.getRolesByName("botjail", true).get(0);
			if(!channel.getName().equals("player-bot-commands") || !fileditor.getgamestate().equals("setup") || message.getMember().getRoles().contains(probation)) { 
				return;
			}
			List<Role> alive = guild.getRolesByName("capture", true);
			guild.getController().removeRolesFromMember(event.getMember(), alive).complete();

			TextChannel announce = guild.getTextChannelsByName("bot-announcements", true).get(0);
			announce.sendMessage(event.getAuthor().getName() + " left the next game!").queue();
		}

		else if (msg.startsWith("!listplayers")) { //can be done at anytime except for endgame
			Role probation = guild.getRolesByName("botjail", true).get(0);
			if(!channel.getName().equals("player-bot-commands") || !fileditor.getgamestate().equals("setup") || message.getMember().getRoles().contains(probation)) { 
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
				if(member.getRoles().contains(winner) && member.getRoles().contains(probation)) {
					guild.getController().modifyMemberRoles(member, limbo, winner, probation).complete(); 
				}
				else if(member.getRoles().contains(winner)) {
					guild.getController().modifyMemberRoles(member, limbo, winner).complete(); 
				}
				else if(member.getRoles().contains(probation)) {
					guild.getController().modifyMemberRoles(member, limbo, probation).complete(); 
				}
				else {
					guild.getController().modifyMemberRoles(member, limbo).complete(); 
				}
				channel.sendMessage(member.getEffectiveName() + ", captured!").queue();
				hbc.sendMessage(thehost.getAsMention() + ", " + member.getEffectiveName() + " has been alleged as captured. Please !styx or !save them asap.").queue();
			}
			
		}
		
		else if (msg.startsWith("!styx")) {
			if(!channel.getName().equals("host-bot-commands")) {
				return;
			}        	
			if(!fileditor.getgamestate().equals("inprogress")) {
				return;
			}
			
			TextChannel announce = guild.getTextChannelsByName("bot-announcements", true).get(0);
			TextChannel hbc = guild.getTextChannelsByName("host-bot-commands", true).get(0);
			Role alive = guild.getRolesByName("capture", true).get(0);
			Role probation = guild.getRolesByName("botjail", true).get(0);
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
					if(member.getRoles().contains(winner) && member.getRoles().contains(probation)) {
						guild.getController().modifyMemberRoles(member, dead, winner, probation).complete(); 
					}
					else if(member.getRoles().contains(winner)) {
						guild.getController().modifyMemberRoles(member, dead, winner).complete(); 
					}
					else if(member.getRoles().contains(probation)) {
						guild.getController().modifyMemberRoles(member, dead, probation).complete(); 
					}
					else {
						guild.getController().modifyMemberRoles(member, dead).complete(); 
					}
					hbc.sendMessage(member.getEffectiveName() + " has been succesfully styxed!").queue();
					winnerexists = (guild.getMembersWithRoles(alive).size() == 1);
				}
			}
			
			if(winnerexists) {
				announce.sendMessage(alive.getAsMention() + ", " + dead.getAsMention() +", the round has ended!\n"
						+ "The winner of this round is " + guild.getMembersWithRoles(alive).get(0).getAsMention() + ".").queue();
				hbc.sendMessage(guild.getMembersWithRoles(hosts).get(0).getAsMention() + ", please end the game with !endgame ASAP!").queue();
				guild.getController().addRolesToMember(guild.getMembersWithRoles(alive).get(0), winner).complete();			
			}
		}
		
		else if (msg.startsWith("!save")) {
			if(!channel.getName().equals("host-bot-commands")) {
				return;
			}        	
			if(!fileditor.getgamestate().equals("inprogress")) {
				return;
			}
			
			TextChannel announce = guild.getTextChannelsByName("bot-announcements", true).get(0);
			TextChannel hbc = guild.getTextChannelsByName("host-bot-commands", true).get(0);
			Role alive = guild.getRolesByName("capture", true).get(0);
			Role probation = guild.getRolesByName("botjail", true).get(0);
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
					if(member.getRoles().contains(winner) && member.getRoles().contains(probation)) {
						guild.getController().modifyMemberRoles(member, alive, winner, probation).complete(); 
					}
					else if(member.getRoles().contains(winner)) {
						guild.getController().modifyMemberRoles(member, alive, winner).complete(); 
					}
					else if(member.getRoles().contains(probation)) {
						guild.getController().modifyMemberRoles(member, alive, probation).complete(); 
					}
					else {
						guild.getController().modifyMemberRoles(member, alive).complete(); 
					}
					hbc.sendMessage(member.getEffectiveName() + " has been succesfully saved!").queue();
					announce.sendMessage(alive.getAsMention() + ", the capture of " + member.getEffectiveName() + " has been deemed faulty.\nThey have been saved and "
							+ "their captor should send the host, " + guild.getMembersWithRoles(hosts).get(0).getEffectiveName() + ", a dm immediately.").queue();
				}
				
			}
		}
		


	}



}

