package com.izofar.bygonenether.block;

import com.izofar.bygonenether.entity.NetheriteBellTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BellAttachment;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class NetheriteBellBlock extends ContainerBlock {

    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final EnumProperty<BellAttachment> ATTACHMENT = BlockStateProperties.BELL_ATTACHMENT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    private static final VoxelShape NORTH_SOUTH_FLOOR_SHAPE = Block.box(0.0D, 0.0D, 4.0D, 16.0D, 16.0D, 12.0D);
    private static final VoxelShape EAST_WEST_FLOOR_SHAPE = Block.box(4.0D, 0.0D, 0.0D, 12.0D, 16.0D, 16.0D);
    private static final VoxelShape BELL_TOP_SHAPE = Block.box(5.0D, 6.0D, 5.0D, 11.0D, 13.0D, 11.0D);
    private static final VoxelShape BELL_BOTTOM_SHAPE = Block.box(4.0D, 4.0D, 4.0D, 12.0D, 6.0D, 12.0D);
    private static final VoxelShape BELL_SHAPE = VoxelShapes.or(BELL_BOTTOM_SHAPE, BELL_TOP_SHAPE);
    private static final VoxelShape NORTH_SOUTH_BETWEEN = VoxelShapes.or(BELL_SHAPE, Block.box(7.0D, 13.0D, 0.0D, 9.0D, 15.0D, 16.0D));
    private static final VoxelShape EAST_WEST_BETWEEN = VoxelShapes.or(BELL_SHAPE, Block.box(0.0D, 13.0D, 7.0D, 16.0D, 15.0D, 9.0D));
    private static final VoxelShape TO_WEST = VoxelShapes.or(BELL_SHAPE, Block.box(0.0D, 13.0D, 7.0D, 13.0D, 15.0D, 9.0D));
    private static final VoxelShape TO_EAST = VoxelShapes.or(BELL_SHAPE, Block.box(3.0D, 13.0D, 7.0D, 16.0D, 15.0D, 9.0D));
    private static final VoxelShape TO_NORTH = VoxelShapes.or(BELL_SHAPE, Block.box(7.0D, 13.0D, 0.0D, 9.0D, 15.0D, 13.0D));
    private static final VoxelShape TO_SOUTH = VoxelShapes.or(BELL_SHAPE, Block.box(7.0D, 13.0D, 3.0D, 9.0D, 15.0D, 16.0D));
    private static final VoxelShape CEILING_SHAPE = VoxelShapes.or(BELL_SHAPE, Block.box(7.0D, 13.0D, 7.0D, 9.0D, 16.0D, 9.0D));


    public NetheriteBellBlock(AbstractBlock.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ATTACHMENT, BellAttachment.FLOOR).setValue(POWERED, Boolean.valueOf(false)));
    }

    @Override
    public void neighborChanged(BlockState blockState, World world, BlockPos blockPos, Block block, BlockPos blockPos1, boolean isMoving) {
        boolean flag = world.hasNeighborSignal(blockPos);
        if (flag != blockState.getValue(POWERED)) {
            if (flag) {
                this.attemptToRing(world, blockPos, null);
            }

            world.setBlock(blockPos, blockState.setValue(POWERED, Boolean.valueOf(flag)), 3);
        }

    }

    @Override
    public void onProjectileHit(World level, BlockState blockState, BlockRayTraceResult traceResult, ProjectileEntity projectile) {
        Entity entity = projectile.getOwner();
        PlayerEntity playerentity = entity instanceof PlayerEntity ? (PlayerEntity)entity : null;
        this.onHit(level, blockState, traceResult, playerentity, true);
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult traceResult) {
        return this.onHit(world, blockState, traceResult, player, true) ? ActionResultType.sidedSuccess(world.isClientSide) : ActionResultType.PASS;
    }

    public boolean onHit(World world, BlockState blockState, BlockRayTraceResult traceResult, @Nullable PlayerEntity player, boolean canRingBell) {
        Direction direction = traceResult.getDirection();
        BlockPos blockpos = traceResult.getBlockPos();
        boolean flag = !canRingBell || this.isProperHit(blockState, direction, traceResult.getLocation().y - (double)blockpos.getY());
        if (flag) {
            boolean flag1 = this.attemptToRing(world, blockpos, direction);
            if (flag1 && player != null) {
                player.awardStat(Stats.BELL_RING);
            }

            return true;
        } else {
            return false;
        }
    }

    private boolean isProperHit(BlockState blockState, Direction direction, double distance) {
        if (direction.getAxis() != Direction.Axis.Y && !(distance > (double)0.8124F)) {
            Direction facingDirection = blockState.getValue(FACING);
            BellAttachment bellattachment = blockState.getValue(ATTACHMENT);
            switch(bellattachment) {
                case FLOOR:
                    return facingDirection.getAxis() == direction.getAxis();
                case SINGLE_WALL:
                case DOUBLE_WALL:
                    return facingDirection.getAxis() != direction.getAxis();
                case CEILING:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public boolean attemptToRing(World world, BlockPos blockPos, @Nullable Direction direction) {
        TileEntity tileentity = world.getBlockEntity(blockPos);
        if (!world.isClientSide && tileentity instanceof NetheriteBellTileEntity) {
            if (direction == null) {
                direction = world.getBlockState(blockPos).getValue(FACING);
            }

            ((NetheriteBellTileEntity)tileentity).onHit(direction);
            world.playSound(null, blockPos, SoundEvents.BELL_BLOCK, SoundCategory.BLOCKS, 4.5F, 0.5F);
            return true;
        } else {
            return false;
        }
    }

    private VoxelShape getVoxelShape(BlockState blockState) {
        Direction direction = blockState.getValue(FACING);
        BellAttachment bellattachment = blockState.getValue(ATTACHMENT);
        if (bellattachment == BellAttachment.FLOOR) {
            return direction != Direction.NORTH && direction != Direction.SOUTH ? EAST_WEST_FLOOR_SHAPE : NORTH_SOUTH_FLOOR_SHAPE;
        } else if (bellattachment == BellAttachment.CEILING) {
            return CEILING_SHAPE;
        } else if (bellattachment == BellAttachment.DOUBLE_WALL) {
            return direction != Direction.NORTH && direction != Direction.SOUTH ? EAST_WEST_BETWEEN : NORTH_SOUTH_BETWEEN;
        } else if (direction == Direction.NORTH) {
            return TO_NORTH;
        } else if (direction == Direction.SOUTH) {
            return TO_SOUTH;
        } else {
            return direction == Direction.EAST ? TO_EAST : TO_WEST;
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, IBlockReader blockReader, BlockPos blockPos, ISelectionContext selectionContext) {
        return this.getVoxelShape(blockState);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, IBlockReader blockReader, BlockPos blockPos, ISelectionContext iSelectionContext) {
        return this.getVoxelShape(blockState);
    }

    @Override
    public BlockRenderType getRenderShape(BlockState blockState) {
        return BlockRenderType.MODEL;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext useContext) {
        Direction direction = useContext.getClickedFace();
        BlockPos blockpos = useContext.getClickedPos();
        World world = useContext.getLevel();
        Direction.Axis direction$axis = direction.getAxis();
        if (direction$axis == Direction.Axis.Y) {
            BlockState blockstate = this.defaultBlockState().setValue(ATTACHMENT, direction == Direction.DOWN ? BellAttachment.CEILING : BellAttachment.FLOOR).setValue(FACING, useContext.getHorizontalDirection());
            if (blockstate.canSurvive(useContext.getLevel(), blockpos)) {
                return blockstate;
            }
        } else {
            boolean flag = direction$axis == Direction.Axis.X && world.getBlockState(blockpos.west()).isFaceSturdy(world, blockpos.west(), Direction.EAST) && world.getBlockState(blockpos.east()).isFaceSturdy(world, blockpos.east(), Direction.WEST) || direction$axis == Direction.Axis.Z && world.getBlockState(blockpos.north()).isFaceSturdy(world, blockpos.north(), Direction.SOUTH) && world.getBlockState(blockpos.south()).isFaceSturdy(world, blockpos.south(), Direction.NORTH);
            BlockState blockstate1 = this.defaultBlockState().setValue(FACING, direction.getOpposite()).setValue(ATTACHMENT, flag ? BellAttachment.DOUBLE_WALL : BellAttachment.SINGLE_WALL);
            if (blockstate1.canSurvive(useContext.getLevel(), useContext.getClickedPos())) {
                return blockstate1;
            }

            boolean flag1 = world.getBlockState(blockpos.below()).isFaceSturdy(world, blockpos.below(), Direction.UP);
            blockstate1 = blockstate1.setValue(ATTACHMENT, flag1 ? BellAttachment.FLOOR : BellAttachment.CEILING);
            if (blockstate1.canSurvive(useContext.getLevel(), useContext.getClickedPos())) {
                return blockstate1;
            }
        }

        return null;
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction facingDirection, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
        BellAttachment bellattachment = blockState.getValue(ATTACHMENT);
        Direction connectedDirection = getConnectedDirection(blockState).getOpposite();
        if (connectedDirection == facingDirection && !blockState.canSurvive(world, currentPos) && bellattachment != BellAttachment.DOUBLE_WALL) {
            return Blocks.AIR.defaultBlockState();
        } else {
            if (facingDirection.getAxis() == blockState.getValue(FACING).getAxis()) {
                if (bellattachment == BellAttachment.DOUBLE_WALL && !facingState.isFaceSturdy(world, facingPos, facingDirection)) {
                    return blockState.setValue(ATTACHMENT, BellAttachment.SINGLE_WALL).setValue(FACING, facingDirection.getOpposite());
                }

                if (bellattachment == BellAttachment.SINGLE_WALL && connectedDirection.getOpposite() == facingDirection && facingState.isFaceSturdy(world, facingPos, blockState.getValue(FACING))) {
                    return blockState.setValue(ATTACHMENT, BellAttachment.DOUBLE_WALL);
                }
            }

            return super.updateShape(blockState, facingDirection, facingState, world, currentPos, facingPos);
        }
    }

    @Override
    public boolean canSurvive(BlockState blockState, IWorldReader worldReader, BlockPos blockPos) {
        Direction direction = getConnectedDirection(blockState).getOpposite();
        return direction == Direction.UP ? Block.canSupportCenter(worldReader, blockPos.above(), Direction.DOWN) : HorizontalFaceBlock.canAttach(worldReader, blockPos, direction);
    }

    private static Direction getConnectedDirection(BlockState blockState) {
        switch(blockState.getValue(ATTACHMENT)) {
            case FLOOR:
                return Direction.UP;
            case CEILING:
                return Direction.DOWN;
            default:
                return blockState.getValue(FACING).getOpposite();
        }
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.DESTROY;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, ATTACHMENT, POWERED);
    }

    @Override
    @Nullable
    public TileEntity newBlockEntity(IBlockReader blockReader) {
        return new NetheriteBellTileEntity();
    }

    @Override
    public boolean isPathfindable(BlockState blockState, IBlockReader blockReader, BlockPos blockPos, PathType pathType) {
        return false;
    }
}
