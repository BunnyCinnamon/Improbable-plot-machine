/*
 * Arekkuusu / Improbable Plot Machine 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;

/*
 * Created by <Arekkuusu> on 28/12/2017.
 * It's distributed as part of Improbable Plot Machine.
 */
public class IPMDamage extends DamageSource {

	public static final DamageSource SHOCK = new IPMDamage("shock").setDamageBypassesArmor().setFireDamage().setDamageIsAbsolute();

	public IPMDamage(String damageTypeIn) {
		super(damageTypeIn);
	}

	@Override
	@Nonnull
	public ITextComponent getDeathMessage(EntityLivingBase entity) {
		EntityLivingBase entitylivingbase = entity.getAttackingEntity();
		String s = "improbableplotmachine.death." + this.damageType;
		String s1 = s + ".player";
		return entitylivingbase != null
				? new TextComponentTranslation(s1, entity.getDisplayName(), entitylivingbase.getDisplayName())
				: new TextComponentTranslation(s, entity.getDisplayName());
	}
}
