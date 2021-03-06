package com.google.code.jtracert.traceBuilder.impl.graph;

import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.model.MethodCallVisitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 * @todo consider using suffix trees or Boyer-Moore algorythm or Knuth-Morris-Pratt algorythm
 * @todo consider using ASM for getting information about recursion, repeating calls, e.t.c.
 */
public class NormalizeMetodCallGraphVisitor implements MethodCallVisitor<Object> {

    private final Map<MethodCall, Integer> methodCallHashCodeMap = new HashMap<MethodCall, Integer>();

    /**
     * @param methodCall
     * @return
     */
    public Object visit(MethodCall methodCall) {

        List<MethodCall> callees = methodCall.getCallees();

        int calleesSize = callees.size();

        for (int i = 0; i < calleesSize; i++) {

            MethodCall callee = callees.get(i);
            int caleeHashCode = getMethodCallHashCode(callee);

            for (int j = i + 1; j < calleesSize; j++) {

                if (j - i > calleesSize - j) break;

                MethodCall nestedCallee = callees.get(j);
                int nestedCalleeHashCode = getMethodCallHashCode(nestedCallee);

                if (caleeHashCode == nestedCalleeHashCode) {
                    boolean equalSequenceFound = true;
                    for (int k = 0; k < j - i && k + j < calleesSize; k++) {
                        equalSequenceFound &=
                                getMethodCallHashCode(callees.get(i + k)).equals(
                                        getMethodCallHashCode(callees.get(j + k)));
                    }
                    if (equalSequenceFound) {
                        calleesSize -= j - i;
                        for (int k = 0; k < j - i; k++) {
                            callees.remove(j);
                        }
                        for (int k = 0; k < j - i; k++) {
                            callees.get(i + k).incrementCallCount();
                        }
                    }
                }

            }

        }

        for (MethodCall callee : methodCall.getCallees()) {
            callee.accept(this);
        }

        return null;
    }

    /**
     * @param methodCall
     * @return
     */
    private Integer getMethodCallHashCode(MethodCall methodCall) {

        if (!methodCallHashCodeMap.containsKey(methodCall)) {
            methodCallHashCodeMap.put(methodCall, methodCall.accept(new HashCodeBuilderMethodCallGraphVisitor()));
        }

        return methodCallHashCodeMap.get(methodCall);

    }

}