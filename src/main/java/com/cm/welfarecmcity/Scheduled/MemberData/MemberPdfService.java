package com.cm.welfarecmcity.Scheduled.MemberData;

import com.cm.welfarecmcity.logic.document.model.DocumentInfoAllRes;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MemberPdfService {

    private final TemplateEngine templateEngine;

    public byte[] generate(List<DocumentInfoAllRes> list, String monthThai, String yearThai)
            throws Exception {

        Context context = new Context();
        context.setVariable(
                "members",
                list
        );
        context.setVariable("month", monthThai);
        context.setVariable("year", yearThai);

        String html = templateEngine.process(
                        "member-info",
                        context
                );

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        PdfRendererBuilder builder = getPdfRendererBuilder(html);
        builder.useFastMode();
        builder.toStream(output);
        builder.run();

        return output.toByteArray();
    }

    private static PdfRendererBuilder getPdfRendererBuilder(String html) throws IOException {
        PdfRendererBuilder builder =
                new PdfRendererBuilder();

        String baseUri = new ClassPathResource("")
                .getURL()
                .toExternalForm();


        builder.withHtmlContent(
                html,
                baseUri
        );

        builder.useFont(
                () -> {
                    try {
                        return new ClassPathResource(
                                "fonts/THSarabun-Regular.ttf"
                        ).getInputStream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                "THSarabun"
        );

        //        builder.useFont(
        //                () -> {
        //                    try {
        //                        return new ClassPathResource(
        //                                "fonts/THSarabun-Bold.ttf"
        //                        ).getInputStream();
        //                    } catch (IOException e) {
        //                        throw new RuntimeException(e);
        //                    }
        //                },
        //                "THSarabun"
        //        );
        return builder;
    }

}
