package file.type.analyzer.algorithms;

import file.type.analyzer.tools.InputParameters;

public interface SearchStrategy {
    boolean applyAlgorithm(byte[] text, String pattern);
}
