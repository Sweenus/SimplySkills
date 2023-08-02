package net.sweenus.simplyskills.rewards;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.SkillsAPI;
import net.puffish.skillsmod.SkillsMod;
import net.puffish.skillsmod.config.ConfigContext;
import net.puffish.skillsmod.json.JsonElementWrapper;
import net.puffish.skillsmod.json.JsonObjectWrapper;
import net.puffish.skillsmod.rewards.Reward;
import net.puffish.skillsmod.rewards.RewardContext;
import net.puffish.skillsmod.utils.JsonParseUtils;
import net.puffish.skillsmod.utils.Result;
import net.puffish.skillsmod.utils.failure.Failure;
import net.puffish.skillsmod.utils.failure.ManyFailures;

import java.util.ArrayList;

public class PassiveSkillReward implements Reward {

    public static final Identifier ID = SkillsMod.createIdentifier("passive_skill");



    public static void register() {
        SkillsAPI.registerRewardWithData(ID, PassiveSkillReward::create);
    }

    private static Result<PassiveSkillReward, Failure> create(JsonElementWrapper rootElement, ConfigContext context) {
        return rootElement.getAsObject().andThen(PassiveSkillReward::create);
    }

    private static Result<PassiveSkillReward, Failure> create(JsonObjectWrapper rootObject) {
        ArrayList<Failure> failures = new ArrayList();
        Result var10000 = rootObject.get("passive_skill").andThen((attributeElement) -> {
            return JsonParseUtils.parseAttribute(attributeElement).andThen((attribute) -> {
                return DefaultAttributeRegistry.get(EntityType.PLAYER).has(attribute) ? Result.success(attribute) : Result.failure(attributeElement.getPath().failureAt("Passive Skill Failure"));
            });
        });
        return failures.isEmpty() ? Result.success(new PassiveSkillReward()) : Result.failure(ManyFailures.ofList(failures));
    }

    public void update(ServerPlayerEntity player, RewardContext context) {
    }

    public void dispose(MinecraftServer server) {
    }
}
