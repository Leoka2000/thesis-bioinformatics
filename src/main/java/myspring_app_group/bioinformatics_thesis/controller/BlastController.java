package myspring_app_group.bioinformatics_thesis.controller;

import demo.NCBIQBlastServiceDemo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BlastController {

    @GetMapping("/blast")
    public String blastForm() {
        return "blastForm";
    }

    @PostMapping("/blast")
    public String blastSubmit(@RequestParam String sequence, Model model) {
        try {
            // Perform BLAST search
            NCBIQBlastServiceDemo blastService = new NCBIQBlastServiceDemo();
            blastService.setSequence(sequence);
            blastService.performBlast();

            // Parse the BLAST output XML file
            File blastOutputFile = new File(NCBIQBlastServiceDemo.BLAST_OUTPUT_FILE);
            List<Map<String, String>> hits = parseBlastOutput(blastOutputFile);

            // Add the hits to the model
            model.addAttribute("hits", hits);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "blastResult";
    }

    private List<Map<String, String>> parseBlastOutput(File xmlFile) throws Exception {
        List<Map<String, String>> hits = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);

        // Get all <Hit> elements
        NodeList hitList = document.getElementsByTagName("Hit");
        for (int i = 0; i < hitList.getLength(); i++) {
            Node hitNode = hitList.item(i);
            if (hitNode.getNodeType() == Node.ELEMENT_NODE) {
                Element hitElement = (Element) hitNode;

                // Extract fields from the <Hit> element
                Map<String, String> hit = new HashMap<>();
                hit.put("num", getElementValue(hitElement, "Hit_num"));
                hit.put("id", getElementValue(hitElement, "Hit_id"));
                hit.put("def", getElementValue(hitElement, "Hit_def"));
                hit.put("accession", getElementValue(hitElement, "Hit_accession"));
                hit.put("len", getElementValue(hitElement, "Hit_len"));

                // Extract HSP (High-Scoring Pair) data
                Element hspElement = (Element) hitElement.getElementsByTagName("Hsp").item(0);
                if (hspElement != null) {
                    hit.put("bitScore", getElementValue(hspElement, "Hsp_bit-score"));
                    hit.put("evalue", getElementValue(hspElement, "Hsp_evalue"));
                    hit.put("identity", getElementValue(hspElement, "Hsp_identity"));
                    hit.put("positive", getElementValue(hspElement, "Hsp_positive"));
                    hit.put("gaps", getElementValue(hspElement, "Hsp_gaps"));
                }

                hits.add(hit);
            }
        }

        return hits;
    }

    private String getElementValue(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }
}