package myspring_app_group.bioinformatics_thesis.controller;

import demo.NCBIQBlastServiceDemo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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

            // Read the BLAST output file
            File blastOutputFile = new File(NCBIQBlastServiceDemo.BLAST_OUTPUT_FILE);
            StringBuilder blastOutput = new StringBuilder();
            try (FileReader reader = new FileReader(blastOutputFile)) {
                int character;
                while ((character = reader.read()) != -1) {
                    blastOutput.append((char) character);
                }
            }

            // Add the BLAST output to the model
            model.addAttribute("blastOutput", blastOutput.toString());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "blastResult";
    }
}