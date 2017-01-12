package cofh.core.fluid;

import cofh.api.core.IInitializer;
import cofh.lib.render.particle.EntityDropParticleFX;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

import static net.minecraft.util.EnumFacing.UP;

public abstract class BlockFluidCoFHBase extends BlockFluidClassic implements IInitializer {

    protected String modName;
    protected String name;

    protected float particleRed = 1.0F;
    protected float particleGreen = 1.0F;
    protected float particleBlue = 1.0F;
    protected boolean shouldDisplaceFluids = false;

    public BlockFluidCoFHBase(Fluid fluid, Material material, String modName, String name) {

        super(fluid, material);

        this.name = name;
        this.modName = modName;

        setUnlocalizedName(modName + ".fluid." + name);
        displacements.put(this, false);
    }

    public BlockFluidCoFHBase(Fluid fluid, Material material, String name) {

        this(fluid, material, "cofh", name);
    }

    public BlockFluidCoFHBase setParticleColor(int c) {

        return setParticleColor(((c >> 16) & 255) / 255f, ((c >> 8) & 255) / 255f, ((c >> 0) & 255) / 255f);
    }

    public BlockFluidCoFHBase setParticleColor(float particleRed, float particleGreen, float particleBlue) {

        this.particleRed = particleRed;
        this.particleGreen = particleGreen;
        this.particleBlue = particleBlue;

        return this;
    }

    public BlockFluidCoFHBase setDisplaceFluids(boolean a) {

        this.shouldDisplaceFluids = a;
        return this;
    }

    @Override
    public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos blockpos, IBlockState iblockstate, Entity entity, double yToTest, Material materialIn, boolean testingHead) {
        if (iblockstate.getMaterial().isLiquid()) {
            double fluidHeight = (double) ((float) (blockpos.getY() + 1) - BlockLiquid.getLiquidHeightPercent(iblockstate.getValue(BlockLiquid.LEVEL)));
            if (yToTest >= fluidHeight) {
                return true;
            }
        }
        return super.isEntityInsideMaterial(world, blockpos, iblockstate, entity, yToTest, materialIn, testingHead);
    }

    //@Override
    //@SideOnly(Side.CLIENT)
    //public IIcon getIcon(int side, int meta) {
    //
    //	return side <= 1 ? IconRegistry.getIcon("Fluid" + name) : IconRegistry.getIcon("Fluid" + name, 1);
    //}

    //@Override
    //@SideOnly(Side.CLIENT)
    //public void registerBlockIcons(IIconRegister ir) {
    //
    //	IconRegistry.addIcon("Fluid" + name, modName + ":fluid/Fluid_" + name + "_Still", ir);
    //	IconRegistry.addIcon("Fluid" + name + "1", modName + ":fluid/Fluid_" + name + "_Flow", ir);
    //}

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

        super.randomDisplayTick(state, world, pos, rand);

        double px = pos.getX() + rand.nextFloat();
        double py = pos.getY() - 1.05D;
        double pz = pos.getZ() + rand.nextFloat();

        if (density < 0) {
            py = pos.getY() + 2.10D;
        }
        if (rand.nextInt(20) == 0 && world.isSideSolid(pos.add(0, densityDir, 0), densityDir == -1 ? UP : EnumFacing.DOWN) && !world.getBlockState(pos.add(0, 2 * densityDir, 0)).getMaterial().blocksMovement()) {
            Particle fx = new EntityDropParticleFX(world, px, py, pz, particleRed, particleGreen, particleBlue, densityDir);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
        }
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {

        return false;
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos pos) {

        if (!shouldDisplaceFluids && world.getBlockState(pos).getMaterial().isLiquid()) {
            return false;
        }
        return super.canDisplace(world, pos);
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos pos) {

        if (!shouldDisplaceFluids && world.getBlockState(pos).getMaterial().isLiquid()) {
            return false;
        }
        return super.displaceIfPossible(world, pos);
    }

    /* IInitializer */
    @Override
    public boolean preInit() {

        return false;
    }

    @Override
    public boolean initialize() {

        return false;
    }

    @Override
    public boolean postInit() {

        return false;
    }

}
