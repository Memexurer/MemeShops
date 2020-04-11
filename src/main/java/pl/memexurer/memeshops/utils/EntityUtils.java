package pl.memexurer.memeshops.utils;

import org.bukkit.entity.Entity;

public final class EntityUtils {
    public static void disableEntityAI(Entity entity) {
        try {
            Object nmsEntity = ReflectionUtils.getCraftbukkitClass("entity.CraftEntity").getMethod("getHandle").invoke(entity);

            Class<?> nbtTagClass = ReflectionUtils.getNMSClass("NBTTagCompound");
            Object tag = nmsEntity.getClass().getMethod("getNBTTag").invoke(nmsEntity);
            if (tag == null) {
                tag = nbtTagClass.getConstructor().newInstance();
            }

            nmsEntity.getClass().getMethod("c", nbtTagClass).invoke(nmsEntity, tag);
            tag.getClass().getMethod("setInt", String.class, int.class).invoke(tag, "NoAI", 1);
            nmsEntity.getClass().getMethod("f", nbtTagClass).invoke(nmsEntity, tag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
