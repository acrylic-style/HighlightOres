package xyz.acrylicstyle.highlightOres.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.ICollectionList;
import util.StringCollection;

import java.util.Locale;

/**
 * Represents sounds (but compatibilities are kept across versions).
 * Sounds annotated with {@link NotNull} is guaranteed to work, on all supported versions (1.8+).
 * Sounds annotated with {@link Nullable} may not work on some specific versions.
 * Be aware: Some note block sounds are appears to missing in 1.9 - 1.11.2; these are annotated with {@link Nullable}.
 */
public class Sound {
    private Sound() {} // don't let anyone create instance of this

    @NotNull public static final ICollectionList<String> sounds = ICollectionList.asList(org.bukkit.Sound.values()).map(Enum::name);
    @NotNull public static final StringCollection<org.bukkit.Sound> knownSounds = new StringCollection<>();

    // =============== Blocks =============== //

    // ----- Note Blocks ----- //

    /**
     * Wood + Note Block
     */
    @NotNull public static final org.bukkit.Sound BLOCK_NOTE_BASS;

    /**
     * Stone + Note Block
     */
    @NotNull public static final org.bukkit.Sound BLOCK_NOTE_BASEDRUM;

    /**
     * Wool + Note Block
     * <p>Missing on 1.9 - 1.11.2
     */
    @Nullable public static final org.bukkit.Sound BLOCK_NOTE_BASS_GUITAR;

    /**
     * Sand + Note Block
     */
    @NotNull public static final org.bukkit.Sound BLOCK_NOTE_SNARE_DRUM;

    /**
     * Dirt, air, grass + Note Block
     */
    @NotNull public static final org.bukkit.Sound BLOCK_NOTE_HARP;

    /**
     * Glowstone Block + Note Block
     */
    @NotNull public static final org.bukkit.Sound BLOCK_NOTE_PLING;

    /**
     * Glass Block + Note Block
     */
    @NotNull public static final org.bukkit.Sound BLOCK_NOTE_HAT;

    /**
     * Soul Sand + Note Block
     * <p>Available on 1.14+
     */
    @Nullable public static final org.bukkit.Sound BLOCK_NOTE_COW_BELL;

    /**
     * Pumpkin + Note Block
     * <p>Available on 1.14+
     */@Nullable public static final org.bukkit.Sound BLOCK_NOTE_DIDGERIDOO;

    /**
     * Emerald Block + Note Block
     * <p>Available on 1.14+
     */
    @Nullable public static final org.bukkit.Sound BLOCK_NOTE_BIT;

    /**
     * Hay Block + Note Block
     * <p>Available on 1.14+
     */
    @Nullable public static final org.bukkit.Sound BLOCK_NOTE_BANJO;

    /**
     * Bone Block + Note Block
     * <p>Available on 1.12+
     */
    @Nullable public static final org.bukkit.Sound BLOCK_NOTE_XYLOPHONE;

    /**
     * Iron Block + Note Block
     * <p>Available on 1.12+
     */
    @Nullable public static final org.bukkit.Sound BLOCK_NOTE_IRON_XYLOPHONE;

    /**
     * Packed Ice Block + Note Block
     * <p>Available on 1.12+
     */
    @Nullable public static final org.bukkit.Sound BLOCK_NOTE_CHIME;

    /**
     * Gold Block + Note Block
     * <p>Available on 1.12+
     */
    @Nullable public static final org.bukkit.Sound BLOCK_NOTE_BELL;

    /**
     * Clay Block + Note Block
     * <p>Available on 1.12+
     */
    @Nullable public static final org.bukkit.Sound BLOCK_NOTE_FLUTE;
    
    // =============== Entities =============== //

    // ----- TNT ----- //

    @NotNull public static final org.bukkit.Sound ENTITY_TNT_PRIMED;

    // ----- Experience Orb ----- //

    @NotNull public static final org.bukkit.Sound ENTITY_EXPERIENCE_ORB_PICKUP;

    // =============== Aliases =============== //

    // ----- Note Block ----- //

    /**
     * Glass Block + Note Block
     */
    @NotNull public static final org.bukkit.Sound BLOCK_NOTE_STICKS;

    /**
     * Stone + Note Block
     */
    @NotNull public static final org.bukkit.Sound BLOCK_NOTE_BASS_DRUM;

    /**
     * Sand + Note Block
     */
    @NotNull public static final org.bukkit.Sound BLOCK_NOTE_SNARE;

    /**
     * Dirt, air, grass + Note Block
     */
    @NotNull public static final org.bukkit.Sound BLOCK_NOTE_PIANO;

    static {
        // resolve sounds

        // all versions
        BLOCK_NOTE_PLING = getSound("NOTE_PLING", "BLOCK_NOTE_BLOCK_PLING", "BLOCK_NOTE_PLING");
        BLOCK_NOTE_BASS = getSound("NOTE_BASS", "BLOCK_NOTE_BLOCK_BASS", "BLOCK_NOTE_BASS");
        BLOCK_NOTE_BASEDRUM = getSound("NOTE_BASS_DRUM", "BLOCK_NOTE_BLOCK_BASEDRUM", "BLOCK_NOTE_BASEDRUM");
        BLOCK_NOTE_SNARE_DRUM = getSound("NOTE_SNARE_DRUM", "BLOCK_NOTE_BLOCK_SNARE", "BLOCK_NOTE_SNARE");
        BLOCK_NOTE_HAT = getSound("NOTE_STICKS", "BLOCK_NOTE_HAT", "BLOCK_NOTE_BLOCK_HAT");
        BLOCK_NOTE_HARP = getSound("NOTE_PIANO", "BLOCK_NOTE_HARP", "BLOCK_NOTE_BLOCK_HARP");

        // basically all versions but not on 1.9.x - 1.11.x
        BLOCK_NOTE_BASS_GUITAR = getSoundNullable("NOTE_BASS_GUITAR", "BLOCK_NOTE_BLOCK_BASS_GUITAR", "BLOCK_NOTE_BASS_GUITAR");

        // 1.14+
        BLOCK_NOTE_COW_BELL = getSoundNullable("NOTE_COW_BELL", "BLOCK_NOTE_BLOCK_COW_BELL", "BLOCK_NOTE_COW_BELL");
        BLOCK_NOTE_DIDGERIDOO = getSoundNullable("NOTE_DIDGERIDOO", "BLOCK_NOTE_BLOCK_DIDGERIDOO", "BLOCK_NOTE_DIDGERIDOO");
        BLOCK_NOTE_BIT = getSoundNullable("NOTE_BIT", "BLOCK_NOTE_BLOCK_BIT", "BLOCK_NOTE_BIT");
        BLOCK_NOTE_BANJO = getSoundNullable("NOTE_BANJO", "BLOCK_NOTE_BLOCK_BANJO", "BLOCK_NOTE_BANJO");

        // 1.12+
        BLOCK_NOTE_XYLOPHONE = getSoundNullable("NOTE_XYLOPHONE", "BLOCK_NOTE_BLOCK_XYLOPHONE", "BLOCK_NOTE_XYLOPHONE");
        BLOCK_NOTE_IRON_XYLOPHONE = getSoundNullable("NOTE_IRON_XYLOPHONE", "BLOCK_NOTE_BLOCK_IRON_XYLOPHONE", "BLOCK_NOTE_IRON_XYLOPHONE");
        BLOCK_NOTE_CHIME = getSoundNullable("NOTE_CHIME", "BLOCK_NOTE_BLOCK_CHIME", "BLOCK_NOTE_CHIME");
        BLOCK_NOTE_BELL = getSoundNullable("NOTE_BELL", "BLOCK_NOTE_BLOCK_BELL", "BLOCK_NOTE_BELL");
        BLOCK_NOTE_FLUTE = getSoundNullable("NOTE_FLUTE", "BLOCK_NOTE_BLOCK_FLUTE", "BLOCK_NOTE_FLUTE");

        ENTITY_TNT_PRIMED = getSound("FUSE", "ENTITY_TNT_PRIMED");
        ENTITY_EXPERIENCE_ORB_PICKUP = getSound("ENTITY_EXPERIENCE_ORB_PICKUP", "ORB_PICKUP");

        // aliases
        BLOCK_NOTE_BASS_DRUM = registerMapping(BLOCK_NOTE_BASEDRUM, "BLOCK_NOTE_BASS_DRUM", "BLOCK_NOTE_BLOCK_BASS_DRUM");
        BLOCK_NOTE_STICKS = registerMapping(BLOCK_NOTE_HAT, "BLOCK_NOTE_STICKS", "BLOCK_NOTE_BLOCK_STICK");
        BLOCK_NOTE_SNARE = registerMapping(BLOCK_NOTE_SNARE_DRUM, "BLOCK_NOTE_SNARE", "BLOCK_NOTE_BLOCK_SNARE_DRUM");
        BLOCK_NOTE_PIANO = registerMapping(BLOCK_NOTE_HARP, "BLOCK_NOTE_PIANO", "BLOCK_NOTE_BLOCK_PIANO");
    }

    @Contract("_, _ -> param1")
    @NotNull
    private static org.bukkit.Sound registerMapping(@NotNull org.bukkit.Sound sound, @NotNull String @NotNull... s) {
        for (String name : s) knownSounds.add(name.toUpperCase(Locale.ROOT), sound);
        return sound;
    }

    /**
     * Try to find sound with specified sounds list.
     * This method might return null if:
     * <ul>
     *     <li>You provided null as fallback and could not find sounds from the list</li>
     * </ul>
     * This method throws IllegalArgumentException, rather than returning null if could not find fallback when required.
     * @deprecated Use {@link #tryResolveSound(String)} unless you have specific reason to use this method
     * @param fallback the fallback sound id when couldn't find sound from sounds list.
     * @param sounds sounds list to find.
     * @return the sound
     * @throws IllegalArgumentException when fallback sound was not found when it tries to find fallback sound
     * @see #tryResolveSound(String)
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    @Nullable
    @Contract("!null, _ -> !null")
    public static org.bukkit.Sound getSound(@Nullable String fallback, @NotNull String @NotNull... sounds) throws IllegalArgumentException {
        org.bukkit.Sound result = null;
        for (String sound : sounds) {
            if (Sound.sounds.contains(sound.toUpperCase(Locale.ROOT))) {
                result = org.bukkit.Sound.valueOf(sound.toUpperCase(Locale.ROOT));
                break;
            }
        }
        if (result == null && fallback != null) {
            result = org.bukkit.Sound.valueOf(fallback.toUpperCase(Locale.ROOT));
        }
        if (result != null) {
            for (String sound : sounds) knownSounds.add(sound, result);
            if (fallback != null) knownSounds.add(fallback, result);
        }
        return result;
    }

    /**
     * Try to find sound with specified sounds list.
     * This method might return null if:
     * <ul>
     *     <li>You provided null as fallback and could not find sounds from the list</li>
     *     <li>Fallback sound was not found in the enum</li>
     * </ul>
     * This method returns null if could not find fallback when required.
     * @deprecated Use {@link #tryResolveSound(String)} unless you have specific reason to use this method
     * @param fallback the fallback sound id when couldn't find sound from sounds list.
     * @param sounds sounds list to find.
     * @return the sound
     * @throws IllegalArgumentException when fallback sound was not found when it tries to find fallback sound
     * @see #tryResolveSound(String)
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    @Nullable
    public static org.bukkit.Sound getSoundNullable(@Nullable String fallback, @NotNull String @NotNull... sounds) throws IllegalArgumentException {
        org.bukkit.Sound result = null;
        for (String sound : sounds) {
            if (Sound.sounds.contains(sound.toUpperCase(Locale.ROOT))) {
                result = org.bukkit.Sound.valueOf(sound.toUpperCase(Locale.ROOT));
                break;
            }
        }
        if (result == null && fallback != null && Sound.sounds.contains(fallback.toUpperCase(Locale.ROOT))) {
            result = org.bukkit.Sound.valueOf(fallback.toUpperCase(Locale.ROOT));
        }
        if (result != null) {
            for (String sound : sounds) knownSounds.add(sound, result);
            if (fallback != null) knownSounds.add(fallback, result);
        }
        return result;
    }

    /**
     * Try to find sound with specified name.
     * This method may return null if:
     * <ul>
     *     <li>There is no sound with that name</li>
     *     <li>That sound is not known in this class, and you have incompatible version with server and sound key.</li>
     * </ul>
     * @param sound the sound name to find
     * @return the sound
     */
    @Nullable
    public static org.bukkit.Sound tryResolveSound(@NotNull String sound) {
        String up = sound.toUpperCase().replaceAll("\\.", "_");
        if (knownSounds.containsKey(up)) return knownSounds.get(up);
        return ICollectionList.asList(org.bukkit.Sound.values()).filter(s -> s.name().toUpperCase().equals(up)).first();
    }
}
