// Copyright (C) 2016 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.gitiles.doc;

import org.commonmark.Extension;
import org.commonmark.node.Block;
import org.commonmark.parser.Parser;
import org.commonmark.parser.Parser.ParserExtension;
import org.commonmark.parser.block.AbstractBlockParser;
import org.commonmark.parser.block.AbstractBlockParserFactory;
import org.commonmark.parser.block.BlockContinue;
import org.commonmark.parser.block.BlockStart;
import org.commonmark.parser.block.MatchedBlockParser;
import org.commonmark.parser.block.ParserState;

/** CommonMark extension for {@code [TOC]}. */
public class TocExtension implements ParserExtension {
  public static Extension create() {
    return new TocExtension();
  }

  private TocExtension() {}

  @Override
  public void extend(Parser.Builder builder) {
    builder.customBlockParserFactory(new TocParserFactory());
  }

  private static class TocParser extends AbstractBlockParser {
    private final TocBlock block = new TocBlock();

    @Override
    public Block getBlock() {
      return block;
    }

    @Override
    public BlockContinue tryContinue(ParserState parserState) {
      return BlockContinue.none();
    }
  }

  private static class TocParserFactory extends AbstractBlockParserFactory {
    @Override
    public BlockStart tryStart(ParserState state, MatchedBlockParser matched) {
      if (state.getIndent() == 0) {
        CharSequence line = state.getLine();
        int s = state.getNextNonSpaceIndex();
        if ("[TOC]".contentEquals(line.subSequence(s, line.length()))) {
          return BlockStart.of(new TocParser()).atIndex(line.length());
        }
      }
      return BlockStart.none();
    }
  }
}
