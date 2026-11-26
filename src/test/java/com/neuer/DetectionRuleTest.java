package com.neuer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.neuer.client.utils.DetectionRule;
import java.util.List;
import org.junit.jupiter.api.Test;

class DetectionRuleTest {

    @Test
    void parsesRulesAndSkipsCommentsBlanksAndMalformedLines() {
        List<DetectionRule> rules = DetectionRule.parseLines(List.of(
                "# comment",
                "",
                "   ",
                "KillAura|play.example.com",
                "  Reach | *example.com  ",
                "no-separator",
                "|missing-module",
                "MissingServer|",
                "*|strict-server.net"));

        assertEquals(3, rules.size());
        assertEquals("killaura", rules.get(0).modulePattern());
        assertEquals("play.example.com", rules.get(0).serverGlob());
        assertEquals("reach", rules.get(1).modulePattern());
        assertEquals("strict-server.net", rules.get(2).serverGlob());
    }

    @Test
    void matchesModuleNameAgainstServerAddress() {
        List<DetectionRule> rules = DetectionRule.parseLines(List.of("KillAura|*example.com"));

        assertTrue(rules.get(0).matches("KillAura", "play.example.com"));
        assertTrue(rules.get(0).matches("killaura", "example.com"));
        assertFalse(rules.get(0).matches("Reach", "play.example.com"));
        assertFalse(rules.get(0).matches("KillAura", "hypixel.net"));
    }

    @Test
    void wildcardModuleMatchesEveryModule() {
        List<DetectionRule> rules = DetectionRule.parseLines(List.of("*|strict-server.net:25565"));

        assertTrue(rules.get(0).matches("KillAura", "strict-server.net:25565"));
        assertTrue(rules.get(0).matches("AutoTotem", "strict-server.net:25565"));
        assertFalse(rules.get(0).matches("AutoTotem", "strict-server.net:25566"));
    }

    @Test
    void toleratesCrLfAndNullInputs() {
        List<DetectionRule> rules = DetectionRule.parse("KillAura|play.example.com\r\nReach|play.example.com\r\n");

        assertEquals(2, rules.size());
        assertTrue(DetectionRule.parse((String) null).isEmpty());
        assertTrue(DetectionRule.parseLines(null).isEmpty());
        assertFalse(rules.get(0).matches(null, "play.example.com"));
        assertFalse(rules.get(0).matches("KillAura", null));
    }
}
