/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nonnull;

/**
 * Created by <Arekkuusu> on 28/12/2017.
 * It's distributed as part of Solar.
 */
public class FixedDamage extends DamageSource {

	public static final DamageSource ELECTRICITY = new FixedDamage("electricity").setDamageBypassesArmor().setFireDamage().setDamageIsAbsolute();

	public FixedDamage(String damageTypeIn) {
		super(damageTypeIn);
	}

	@Override
	@Nonnull
	public ITextComponent getDeathMessage(EntityLivingBase entity) {
		EntityLivingBase entitylivingbase = entity.getAttackingEntity();
		String s = "solar.death." + this.damageType;
		String s1 = s + ".player";
		return entitylivingbase != null && I18n.canTranslate(s1)
				? new TextComponentTranslation(s1, entity.getDisplayName(), entitylivingbase.getDisplayName())
				: new TextComponentTranslation(s, entity.getDisplayName());
	}
}
