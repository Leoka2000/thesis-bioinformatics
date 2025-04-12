package myspring_app_group.bioinformatics_thesis.controller;

import demo.NCBIQBlastServiceDemo;
import myspring_app_group.bioinformatics_thesis.model.BlastResult;
import myspring_app_group.bioinformatics_thesis.repository.BlastResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class BlastController {

    @Autowired
    private BlastResultRepository blastResultRepository;

    @GetMapping("/")
    public String redirectToBlast() {
        return "redirect:/blast";
    }

    @GetMapping("/blast")
    public String blastForm(Model model) {

        List<BlastResult> allResults = blastResultRepository.findAll();
        Map<Date, List<BlastResult>> groupedResults = allResults.stream()
                .collect(Collectors.groupingBy(BlastResult::getTimestamp));

        List<Map<String, Object>> pastSearches = new ArrayList<>();
        for (Map.Entry<Date, List<BlastResult>> entry : groupedResults.entrySet()) {
            Map<String, Object> search = new HashMap<>();
            search.put("id", entry.getKey().getTime()); // Use timestamp as ID
            search.put("sequence", entry.getValue().get(0).getSequence()); // All results in a search have the same
                                                                           // sequence
            search.put("timestamp", entry.getKey());
            search.put("results", entry.getValue());
            pastSearches.add(search);
        }

        model.addAttribute("pastSearches", pastSearches);
        return "blastForm";
    }

    @PostMapping("/blast")
    public String blastSubmit(@RequestParam String sequence, Model model) {
        try {
            NCBIQBlastServiceDemo blastService = new NCBIQBlastServiceDemo();
            blastService.setSequence(sequence);
            blastService.performBlast();

            File blastOutputFile = new File(NCBIQBlastServiceDemo.BLAST_OUTPUT_FILE);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(blastOutputFile);

            Map<String, String> blastOutputInfo = new HashMap<>();
            blastOutputInfo.put("program", getElementValue(document.getDocumentElement(), "BlastOutput_program"));
            blastOutputInfo.put("version", getElementValue(document.getDocumentElement(), "BlastOutput_version"));
            blastOutputInfo.put("reference", getElementValue(document.getDocumentElement(), "BlastOutput_reference"));
            blastOutputInfo.put("db", getElementValue(document.getDocumentElement(), "BlastOutput_db"));
            blastOutputInfo.put("queryId", getElementValue(document.getDocumentElement(), "BlastOutput_query-ID"));
            blastOutputInfo.put("queryDef", getElementValue(document.getDocumentElement(), "BlastOutput_query-def"));
            blastOutputInfo.put("queryLen", getElementValue(document.getDocumentElement(), "BlastOutput_query-len"));

            Element parametersElement = (Element) document.getDocumentElement().getElementsByTagName("Parameters")
                    .item(0);
            if (parametersElement != null) {
                blastOutputInfo.put("matrix", getElementValue(parametersElement, "Parameters_matrix"));
                blastOutputInfo.put("expect", getElementValue(parametersElement, "Parameters_expect"));
                blastOutputInfo.put("gapOpen", getElementValue(parametersElement, "Parameters_gap-open"));
                blastOutputInfo.put("gapExtend", getElementValue(parametersElement, "Parameters_gap-extend"));
                blastOutputInfo.put("filter", getElementValue(parametersElement, "Parameters_filter"));
            }

            model.addAttribute("blastOutputInfo", blastOutputInfo);

            List<Map<String, String>> hits = parseBlastOutput(blastOutputFile);
            for (Map<String, String> hit : hits) {
                BlastResult result = new BlastResult();
                result.setSequence(sequence);
                result.setHitId(hit.get("id"));
                result.setHitDefinition(hit.get("def"));
                result.setHitAccession(hit.get("accession"));
                result.setHitLength(Integer.parseInt(hit.get("len")));
                result.setBitScore(Double.parseDouble(hit.get("bitScore")));
                result.setEvalue(Double.parseDouble(hit.get("evalue")));
                result.setIdentity(Integer.parseInt(hit.get("identity")));
                result.setPositive(Integer.parseInt(hit.get("positive")));
                result.setGaps(Integer.parseInt(hit.get("gaps")));
                result.setTimestamp(new Date());

                // Set the BLAST output information
                result.setProgram(blastOutputInfo.get("program"));
                result.setVersion(blastOutputInfo.get("version"));
                result.setReference(blastOutputInfo.get("reference"));
                result.setDb(blastOutputInfo.get("db"));
                result.setQueryId(blastOutputInfo.get("queryId"));
                result.setQueryDef(blastOutputInfo.get("queryDef"));
                result.setQueryLen(blastOutputInfo.get("queryLen"));
                result.setMatrix(blastOutputInfo.get("matrix"));
                result.setExpect(blastOutputInfo.get("expect"));
                result.setGapOpen(blastOutputInfo.get("gapOpen"));
                result.setGapExtend(blastOutputInfo.get("gapExtend"));
                result.setFilter(blastOutputInfo.get("filter"));

                blastResultRepository.save(result);
            }

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

        NodeList hitList = document.getElementsByTagName("Hit");
        for (int i = 0; i < hitList.getLength(); i++) {
            Node hitNode = hitList.item(i);
            if (hitNode.getNodeType() == Node.ELEMENT_NODE) {
                Element hitElement = (Element) hitNode;
                Map<String, String> hit = new HashMap<>();
                hit.put("num", getElementValue(hitElement, "Hit_num"));
                hit.put("id", getElementValue(hitElement, "Hit_id"));
                hit.put("def", getElementValue(hitElement, "Hit_def"));
                hit.put("accession", getElementValue(hitElement, "Hit_accession"));
                hit.put("len", getElementValue(hitElement, "Hit_len"));

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