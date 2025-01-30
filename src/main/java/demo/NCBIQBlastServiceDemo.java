package demo;

import org.biojava.nbio.core.sequence.io.util.IOUtils;
import org.biojava.nbio.ws.alignment.qblast.BlastProgramEnum;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastAlignmentProperties;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastOutputProperties;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastService;

import java.io.*;

import static org.biojava.nbio.ws.alignment.qblast.BlastAlignmentParameterEnum.ENTREZ_QUERY;

public class NCBIQBlastServiceDemo {
    public static final String BLAST_OUTPUT_FILE = "blastOutput.xml";
    private String sequence;

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public void performBlast() throws Exception {
        NCBIQBlastService service = new NCBIQBlastService();

        // set alignment options
        NCBIQBlastAlignmentProperties props = new NCBIQBlastAlignmentProperties();
        props.setBlastProgram(BlastProgramEnum.blastp);
        props.setBlastDatabase("swissprot");
        props.setAlignmentOption(ENTREZ_QUERY, "");

        // set output options
        NCBIQBlastOutputProperties outputProps = new NCBIQBlastOutputProperties();

        String rid = null;
        FileWriter writer = null;
        BufferedReader reader = null;
        try {
            // send blast request and save request id
            rid = service.sendAlignmentRequest(sequence, props);

            while (!service.isReady(rid)) {
                System.out.println("Waiting for results. Sleeping for 2 seconds");
                Thread.sleep(2000);
            }

            // read results when they are ready
            InputStream in = service.getAlignmentResults(rid, outputProps);
            reader = new BufferedReader(new InputStreamReader(in));

            File f = new File(BLAST_OUTPUT_FILE);
            System.out.println("Saving query results in file " + f.getAbsolutePath());
            writer = new FileWriter(f);

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line + System.getProperty("line.separator"));
            }
        } finally {
            IOUtils.close(writer);
            IOUtils.close(reader);
            service.sendDeleteRequest(rid);
        }
    }
}