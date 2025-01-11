# Java Discord Role Management Bot

A robust Java-based Discord bot designed to give users controlled access to manage roles, kick, and ban members while restricting access to built-in Discord functions. The bot ensures granular control and customization, making it ideal for server moderation without compromising security.

## Features

- **Role Management**  
  - Create, assign, unassign, and manage roles.
  - Temporarily assign roles for a specified duration.
  - Copy permissions between roles.
  - View detailed role information and members with specific roles.
  - Set and remove role managers.

- **Member Moderation**  
  - Kick and ban members from the server.
  - Unban members using their ID.

- **Permission System**  
  - Customizable permissions to restrict user actions.
  - List permissions assigned to roles or users.
  - View all permissions configured in the server.

- **Embeds & Giveaways**  
  - Create and edit visually appealing embeds.
  - Manage giveaways with detailed options.

- **Customization**  
  - Set a request channel for role-related actions.
  - Fine-tune bot behavior with a secure permissions system.

## Commands

| Command              | Description                                           |
|----------------------|-------------------------------------------------------|
| `/about`             | Provides information about the bot.                  |
| `/kick`              | Kicks a member from the server.                      |
| `/ban`               | Bans a member from the server.                       |
| `/unban`             | Unbans a member from the server.                     |
| `/permissions`       | Sets permissions for roles or users.                 |
| `/copyperms`         | Copies permissions from one role to another.         |
| `/setrequestchannel` | Sets the channel for role requests.                  |
| `/listperms`         | Lists permissions for a role or user.                |
| `/listallperms`      | Lists all permissions given to any user or role.     |
| `/viewusers`         | Views all members with the selected role.            |
| `/roleinfo`          | Views detailed information about a role.             |
| `/setmanager`        | Sets a role manager.                                 |
| `/removemanager`     | Removes a role manager.                              |
| `/createembed`       | Creates a custom embed message.                      |
| `/editembed`         | Edits an existing embed message.                     |
| `/createrole`        | Creates a new role with specified options.           |
| `/assignrole`        | Assigns a role to a user.                            |
| `/unassignrole`      | Removes a role from a user.                          |
| `/temprole`          | Temporarily assigns a role to a user.                |
| `/giveaway`          | Starts a giveaway with configurable options.         |

## Dependencies

The following libraries are used in this project:

```xml
<dependency>
    <groupId>net.dv8tion</groupId>
    <artifactId>JDA</artifactId>
    <version>5.0.0-beta.12</version>
</dependency>

<dependency>
    <groupId>io.github.cdimascio</groupId>
    <artifactId>dotenv-java</artifactId>
    <version>3.0.0</version>
</dependency>

<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.11.0</version>
</dependency>

<dependency>
    <groupId>com.opencsv</groupId>
    <artifactId>opencsv</artifactId>
    <version>5.9</version>
</dependency>
