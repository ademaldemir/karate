package com.intuit.karate.core;

import com.intuit.karate.match.Match;
import com.intuit.karate.match.MatchResult;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pthomas3
 */
class FeatureRuntimeTest {

    static final Logger logger = LoggerFactory.getLogger(FeatureRuntimeTest.class);

    boolean fail;
    FeatureRuntime fr;

    @BeforeEach
    void beforeEach() {
        fail = false;
    }

    private FeatureRuntime run(String name) {
        return run(name, null);
    }

    private FeatureRuntime run(String name, String configDir) {
        fr = RuntimeUtils.runFeature("classpath:com/intuit/karate/core/" + name, configDir);
        if (fail) {
            assertTrue(fr.result.isFailed());
        } else {
            assertFalse(fr.result.isFailed());
        }
        return fr;
    }

    private File report() {
        File file = HtmlFeatureReport.saveFeatureResult("target/temp", fr.result);
        logger.debug("saved report: {}", file.getAbsolutePath());
        return file;
    }

    private void match(Object actual, Object expected) {
        MatchResult mr = Match.that(actual).isEqualTo(expected);
        assertTrue(mr.pass, mr.message);
    }

    private void matchContains(Object actual, Object expected) {
        MatchResult mr = Match.that(actual).contains(expected);
        assertTrue(mr.pass, mr.message);
    }

    @Test
    void testPrint() {
        run("print.feature");
    }

    @Test
    void testFail1() {
        fail = true;
        run("fail1.feature");
    }

    @Test
    void testCallOnce() {
        run("callonce-bg.feature");
    }

    @Test
    void testCallOnceWithUtilsPresentInKarateConfig() {
        run("callonce-bg.feature", "classpath:com/intuit/karate/core");
    }

    @Test
    void testCallOnceGlobal() {
        run("callonce-global.feature");
    }

    @Test
    void testTags() {
        run("tags.feature");
        match(fr.getResult(), "{ configSource: 'normal', tagNames: ['two=foo,bar', 'one'], tagValues: { one: [], two: ['foo', 'bar'] } }");
    }

    @Test
    void testAbort() {
        run("abort.feature");
        match(fr.getResult(), "{ configSource: 'normal', before: true }");
    }

    @Test
    void testFailApi() {
        fail = true;
        run("fail-api.feature");
        match(fr.getResult(), "{ configSource: 'normal', before: true }");
    }

    @Test
    void testCallFeatureFromJs() {
        run("call-js.feature");
        matchContains(fr.getResult(), "{ calledVar: 'hello world' }");
    }

    @Test
    void testCallJsFromFeatureUtilsDefinedInKarateConfig() {
        run("karate-config-fn.feature", "classpath:com/intuit/karate/core/");
        matchContains(fr.getResult(), "{ helloVar: 'hello world' }");
    }

    @Test
    void testCallByTag() {
        run("call-by-tag.feature");
    }

    @Test
    void testCopyAndClone() {
        run("copy.feature");
    }

    @Test
    void testMatchEachMagicVariables() {
        run("match-each-magic-variables.feature");
    }

    @Test
    void testEvalAndSet() {
        run("eval-and-set.feature");
    }

    @Test
    void testExtract() {
        run("extract.feature");
    }

    @Test
    void testConfigureInJs() {
        run("configure-in-js.feature");
    }

    @Test
    void testTable() {
        run("table.feature");
    }

    @Test
    void testSet() {
        run("set.feature");
    }

    @Test
    void testSetXml() {
        run("set-xml.feature");
    }

    @Test
    void testJsRead() {
        run("js-read.feature");
    }

    @Test
    void testJsMapRepeat() {
        run("js-map-repeat.feature");
    }

    @Test
    void testCallFeature() {
        run("call-feature.feature");
    }

    @Test
    void testOutlineGenerator() {
        run("outline-generator.feature");
    }
    
    @Test
    void testToBean() {
        run("to-bean.feature");
    }    

}