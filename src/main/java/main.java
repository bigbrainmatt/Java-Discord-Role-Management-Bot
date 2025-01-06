import general.about;
import general.StartAndEnd;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import roleRelated.*;
import io.github.cdimascio.dotenv.Dotenv;
import modderation.ban;
import modderation.kick;
import modderation.unban;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class main {
    private final ShardManager shardManager;
    Dotenv dotenv = Dotenv.load();
    private final String token = dotenv.get("DISCORD_TOKEN");
    public main() throws LoginException {

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES);
        builder.setEventPassthrough(true);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.ROLE_TAGS);
        shardManager = builder.build();

        shardManager.addEventListener(
                new commandManager(),
                new about(),
                new kick(),
                new ban(),
                new unban(),
                new permissions(),
                new StartAndEnd(),
                new copyPerms(),
                new setRequestChannel(),
                new listPerms(),
                new listallperms(),
                new viewUsers()
        );

    }
    public ShardManager getShardManager() { return shardManager; }

    public static void main(String[] args) throws IOException {
        try {
            main bot = new main();
            System.out.println("Sucess: your bot is online");
        } catch (LoginException e) {
            System.out.println("ERROR: Provided bot token is invalid!");
        }
    }
}
