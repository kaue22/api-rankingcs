package rankingcs.adapter.in.utils;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DateValidator {

    public String findDate(String content) {
        String format = "yyyy-MM-dd";

        String dateFound = captureDate(content, format);
        if (dateFound != null) {
            return dateFound;
        }
        System.out.println("Nenhuma data válida encontrada.");
        return "";
    }

    public String captureDate(String texto, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false); // Define como rigoroso para evitar datas inválidas como 32/01/2023

        // Cria um padrão regex para encontrar datas no formato fornecido
        String padraoRegex = "\\b\\d{4}-\\d{2}-\\d{2}\\b";
        Pattern padrao = Pattern.compile(padraoRegex);
        Matcher matcher = padrao.matcher(texto);

        while (matcher.find()) {
            String candidateDate = matcher.group();
            try {
                Date dataFormatada = sdf.parse(candidateDate);
                // Verifique se a string de entrada corresponde exatamente ao formato
                if (candidateDate.equals(sdf.format(dataFormatada))) {
                    return candidateDate;
                }
            } catch (ParseException e) {
                // Continua a procurar outras correspondências
            }
        }
        return null; // Se nenhuma data válida for encontrada
    }
}
