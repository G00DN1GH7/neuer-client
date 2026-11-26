package com.neuer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.neuer.client.utils.ServerGlob;
import java.util.List;
import org.junit.jupiter.api.Test;

class ServerGlobTest {

    @Test
    void matchesExactAddressCaseInsensitively() {
        assertTrue(ServerGlob.matches("play.example.com", "play.example.com"));
        assertTrue(ServerGlob.matches("PLAY.Example.com", "play.example.COM"));
        assertTrue(ServerGlob.matches("play.example.com.", "play.example.com"));
        assertFalse(ServerGlob.matches("play.example.com", "other.example.com"));
    }

    @Test
    void wildcardMatchesAnyPrefixIncludingTheBareDomain() {
        assertTrue(ServerGlob.matches("*example.com", "play.example.com"));
        assertTrue(ServerGlob.matches("*example.com", "example.com"));
        assertFalse(ServerGlob.matches("*example.com", "example.com.evil.net"));
    }

    @Test
    void portIsOptionalInPatterns() {
        assertTrue(ServerGlob.matches("play.example.com", "play.example.com:25565"));
        assertTrue(ServerGlob.matches("play.example.com:25565", "play.example.com:25565"));
        assertFalse(ServerGlob.matches("play.example.com:25565", "play.example.com:25566"));
        assertTrue(ServerGlob.matches("*example.com", "play.example.com:25565"));
    }

    @Test
    void ignoresTrailingPaths() {
        assertTrue(ServerGlob.matches("play.example.com", "play.example.com/lobby"));
        assertEquals("play.example.com", ServerGlob.hostOf("play.example.com:25565/lobby"));
    }

    @Test
    void matchesAnyPatternInAList() {
        List<String> globs = List.of("*mcpvp.club", "*catpvp.xyz");

        assertTrue(ServerGlob.matchesAny(globs, "mc.mcpvp.club"));
        assertTrue(ServerGlob.matchesAny(globs, "catpvp.xyz"));
        assertFalse(ServerGlob.matchesAny(globs, "hypixel.net"));
        assertFalse(ServerGlob.matchesAny(List.of(), "hypixel.net"));
        assertFalse(ServerGlob.matchesAny(null, "hypixel.net"));
    }

    @Test
    void splitListDropsBlankEntries() {
        assertEquals(List.of("a", "b"), ServerGlob.splitList(" a, ,b ,"));
        assertEquals(List.of(), ServerGlob.splitList("  "));
        assertEquals(List.of(), ServerGlob.splitList(null));
    }

    @Test
    void ignoresNullAndBlankInputs() {
        assertFalse(ServerGlob.matches(null, "play.example.com"));
        assertFalse(ServerGlob.matches("play.example.com", null));
        assertFalse(ServerGlob.matches("  ", "play.example.com"));
    }
}
