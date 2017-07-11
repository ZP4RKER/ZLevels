package me.zp4rker.zlevels.lstnr;

import me.zp4rker.core.logger.ZLogger;
import me.zp4rker.zlevels.ZLevels;
import me.zp4rker.zlevels.cmd.*;
import me.zp4rker.zlevels.config.Config;
import me.zp4rker.zlevels.db.Database;
import me.zp4rker.zlevels.db.StaffRating;
import me.zp4rker.zlevels.db.UserData;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

/**
 * Runs when bot has started.
 *
 * @author ZP4RKER
 */
public class ReadyListener {

    /**
     * Handles the event of the bot being ready.
     *
     * @param event The event to handle.
     */
    @SubscribeEvent
    public void onReady(ReadyEvent event) {
        ZLogger.info("Registering comands...");

        ZLevels.handler.registerCommand(new HelpCommand());
        ZLevels.handler.registerCommand(new RankCommand());
        ZLevels.handler.registerCommand(new TopCommand());
        ZLevels.handler.registerCommand(new LeaderboardCommand());
        ZLevels.handler.registerCommand(new RatingCommand());
        ZLevels.handler.registerCommand(new RewardsCommand());
        ZLevels.handler.registerCommand(new InactiveCommand());
        ZLevels.handler.registerCommand(new FlushCommand());
        ZLevels.handler.registerCommand(new StopCommand());
        ZLevels.handler.registerCommand(new TestCommand());

        ZLogger.info("Successfully registered " + ZLevels.handler.getCommands().values().size() + " cmd!");

        if (!Config.GAME_STATUS.isEmpty()) {
            ZLogger.info("Setting game status...");

            event.getJDA().getPresence().setGame(new Game() {
                @Override
                public String getName() {
                    return Config.GAME_STATUS;
                }

                @Override
                public String getUrl() {
                    return null;
                }

                @Override
                public GameType getType() {
                    return GameType.DEFAULT;
                }
            });
        }

        ZLevels.async.submit(() -> {
            Database.load();

            StaffRating.startMonth();

            UserData.startFlushTimer();

            ZLogger.info("ZLevels " + ZLevels.VERSION + " is ready!");
        });
    }

}