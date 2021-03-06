package com.google.code.jtracert.traceBuilder.impl.sdedit;

import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.util.FileUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class SDEditOutClient extends BaseSDEditClient {

    /**
     * @param methodCall
     */
    @Override
    public void processMethodCall(MethodCall methodCall) {

        try {

            Writer diagramWriter = new OutputStreamWriter(System.out);

            diagramWriter.append("user:Actor").append(FileUtils.LINE_SEPARATOR);

            writeObjectNames(methodCall, diagramWriter);

            diagramWriter.append(FileUtils.LINE_SEPARATOR);

            diagramWriter.
                    append("user:").
                    append(methodCall.getClassName().replaceAll("\\.", "\\\\.")).
                    append(".").
                    append(methodCall.getMethodName()).
                    append(FileUtils.LINE_SEPARATOR);

            writeMethodNames(methodCall, diagramWriter);

            diagramWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}