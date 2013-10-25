package uk.ac.cam.cimr.autophagy.scoring;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Uses a regexp based dictionary to score text, according to provided scoring values for each pattern
 *
 *
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 25/10/13
 * Time: 13:30
 * To change this template use File | Settings | File Templates.
 */
public class SimpleDictionaryBasedAssayScorer implements BioAssayScorer {

    private static final Logger LOGGER = Logger.getLogger(SimpleDictionaryBasedAssayScorer.class);

    private Map<WrappedPattern,Integer> keyword2score;


    /**
     * Constructor based on an input stream leading the dictionary data.
     *
     * @param in
     */
    public SimpleDictionaryBasedAssayScorer(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        fillDictionary(reader);
    }


    /**
     * Fills a dictionary of regular expressions.
     *
     * @param reader
     */
    private void fillDictionary(BufferedReader reader) {
        keyword2score = new HashMap<WrappedPattern,Integer>();
        try {
            reader.readLine();
            String line;
            while ((line = reader.readLine())!=null) {
                String[] tokens = line.split("\t");
                try {
                    keyword2score.put(new WrappedPattern(Pattern.compile("[\\s\\p{Punct}]" + tokens[0].toLowerCase() + "[\\s\\p{Punct}]", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE))
                            , Integer.parseInt(tokens[1]));
                } catch (NumberFormatException e) {
                }
            }
        } catch (IOException e) {
            LOGGER.error("Could not load dictionary from provided file",e);
            throw new RuntimeException("Could not load dictionary for SimpleDictionaryBasedAssayScorer");
        } finally {
            try {
                reader.close();
            } catch (IOException e) {

            }
        }

    }

    @Override
    public Integer getScore(String txt) {
        Integer score = 0;
        for (WrappedPattern pat : keyword2score.keySet()) {
            if(pat.split(txt).length > 1) {
                Integer s = keyword2score.get(pat);
                if(s < 30) {
                    System.out.println("Pattern "+pat.p.pattern()+" found.");
                }
                score += s;
            }
        }
        return score;
    }

    /**
     * We have a wrapped class of pattern where we control the way hash code and equals behave (based on the string
     * pattern).
     *
     */
    private class WrappedPattern {

        Pattern p;

        public WrappedPattern(Pattern p) {
            this.p = p;
        }

        public String[] split(CharSequence seq) {
            return this.p.split(seq);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof WrappedPattern)) return false;

            WrappedPattern that = (WrappedPattern) o;

            if (!p.pattern().equals(that.p.pattern())) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return p.pattern().hashCode();
        }
    }
}
