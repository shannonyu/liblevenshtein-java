package com.github.dylon.liblevenshtein.collection.dawg.factory;

import java.util.Queue;
import java.util.ArrayDeque;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectRBTreeMap;

import com.github.dylon.liblevenshtein.collection.dawg.DawgNode;

/**
 * Builds nodes for use in DAWG structures. This implementation uses an object
 * pool to avoid unnecessary memory consumption and garbage collection.<br>
 * <br>
 * <b>WARNING:</b> This implementation is not thread-safe.
 *
 * @author Dylon Edwards
 * @since 2.1.0
 */
public class DawgNodeFactory implements IDawgNodeFactory<DawgNode> {

	/**
	 * Caches {@link DawgNode} instances to recycle from {@link #build()}.
	 */
  private final Queue<DawgNode> queue = new ArrayDeque<>();

  /**
   * {@inheritDoc}
   */
  @Override
  public DawgNode build() {
    DawgNode node = queue.poll();
    if (null == node) {
      final Char2ObjectMap<DawgNode> edges = new Char2ObjectRBTreeMap<>();
      node = new DawgNode(edges);
    }
    return node;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DawgNodeFactory recycle(final DawgNode node) {
    node.clear();
    queue.offer(node);
    return this;
  }
}
