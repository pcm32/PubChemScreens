package uk.ac.cam.cimr.autophagy.exec;

import com.google.common.base.Joiner;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.metabolomes.webservices.EUtilsWebServiceConnection;
import uk.ac.ebi.metabolomes.webservices.eutils.PubChemBioAssayESummaryResult;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 16/10/13
 * Time: 13:26
 * To change this template use File | Settings | File Templates.
 */
public class FixMissingAssayMetadata {

    private String assayFilePath;
    private String header;
    private Map<String,String> id2data = new HashMap<String,String>();

    public FixMissingAssayMetadata(String assayFilePath) {
        this.assayFilePath = assayFilePath;
    }

    public void run() {
        /**
         * Lets read the file to fix
         */
        EUtilsWebServiceConnection con = new EUtilsWebServiceConnection();
        List<String> aids = readsAssayFile();
        List<PubChemBioAssayESummaryResult> results = con.getPubChemBioassaysSummaries(aids);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(assayFilePath+".fixed"));
            writer.write(header+"\n");
            for(int i=0;i<results.size();i++) {
                writer.write(hyperlinked(aids.get(i))+"\t"+results.get(i).getName()
                        +"\t"+results.get(i).getDescription()
                        +"\t"+id2data.get(aids.get(i))+"\n");
            }

            writer.close();
        } catch (IOException e) {

        }

    }

    private String hyperlinked(String aid) {
        return "=HYPERLINK(\"http://pubchem.ncbi.nlm.nih.gov/assay/assay.cgi?aid="+aid+"\","+aid+")";
    }

    public static void main(String[] args) {
        String pathAssay = args[0];

        FixMissingAssayMetadata fixer = new FixMissingAssayMetadata(pathAssay);
        fixer.run();
    }

    private List<String> readsAssayFile() {
        List<String> aids = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(assayFilePath));
            header = reader.readLine();
            String line;
            while ( (line = reader.readLine())!=null ) {
                String[] tokens = line.split("\t");
                String data = Joiner.on("\t").join(Arrays.copyOfRange(tokens, 3, tokens.length));
                String id = parseID(tokens[0]);
                id2data.put(id,data);
                aids.add(id);
            }
            reader.close();
        } catch (IOException e) {

        }
        return aids;
    }

    private String parseID(String token) {
        if(token.contains("HYPERLINK")) {
            String[] elements = token.split(",");
            return elements[1].replaceAll("\"", "").replaceAll("\\)","");
        }
        return token;
    }

}
