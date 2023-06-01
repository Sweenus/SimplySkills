package net.sweenus.simplyskills.rewards;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.SkillsAPI;
import net.puffish.skillsmod.SkillsMod;
import net.puffish.skillsmod.json.JsonElementWrapper;
import net.puffish.skillsmod.json.JsonObjectWrapper;
import net.puffish.skillsmod.rewards.Reward;
import net.puffish.skillsmod.rewards.RewardContext;
import net.puffish.skillsmod.rewards.builtin.AttributeReward;
import net.puffish.skillsmod.utils.Result;
import net.puffish.skillsmod.utils.error.Error;
import net.puffish.skillsmod.utils.error.ManyErrors;

import java.util.ArrayList;

public class PassiveSkillReward implements Reward {

    public static final Identifier ID = SkillsMod.createIdentifier("passive_skill");


    public static void registerSkillTypes() {
        SkillsAPI.registerReward(ID, PassiveSkillReward::create);
    }

    private static Result<PassiveSkillReward, Error> create(Result<JsonElementWrapper, Error> maybeDataElement) {
        return maybeDataElement.andThen(PassiveSkillReward::create);
    }

    private static Result<PassiveSkillReward, Error> create(JsonElementWrapper rootElement) {
        return rootElement.getAsObject().andThen(PassiveSkillReward::create);
    }

    private static Result<PassiveSkillReward, Error> create(JsonObjectWrapper rootObject) {
        var errors = new ArrayList<Error>();

        var optCommand = rootObject.getString("passive_skill")
                .ifFailure(errors::add)
                .getSuccess();

        if (errors.isEmpty()) {
            return Result.success(new PassiveSkillReward());
        } else {
            return Result.failure(ManyErrors.ofList(errors));
        }
    }

    @Override
    public void update(ServerPlayerEntity serverPlayerEntity, RewardContext rewardContext) {

    }

    @Override
    public void dispose(MinecraftServer minecraftServer) {

    }
}
