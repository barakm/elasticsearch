/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.search.aggregations.support;

import org.elasticsearch.search.DocValueFormat;
import org.elasticsearch.search.aggregations.Aggregator;
import org.elasticsearch.search.aggregations.AggregatorFactories;
import org.elasticsearch.search.aggregations.AggregatorFactory;
import org.elasticsearch.search.aggregations.CardinalityUpperBound;

import java.io.IOException;
import java.util.Map;

public abstract class MultiValuesSourceAggregatorFactory extends AggregatorFactory {

    protected final Map<String, ValuesSourceConfig> configs;
    protected final DocValueFormat format;

    public MultiValuesSourceAggregatorFactory(String name, Map<String, ValuesSourceConfig> configs,
                                              DocValueFormat format, AggregationContext context,
                                              AggregatorFactory parent, AggregatorFactories.Builder subFactoriesBuilder,
                                              Map<String, Object> metadata) throws IOException {
        super(name, context, parent, subFactoriesBuilder, metadata);
        this.configs = configs;
        this.format = format;
    }

    @Override
    public Aggregator createInternal(Aggregator parent, CardinalityUpperBound cardinality, Map<String, Object> metadata)
        throws IOException {
        return doCreateInternal(configs, format, parent, cardinality, metadata);
    }

    /**
     * Create an aggregator that won't collect anything but will return an
     * appropriate empty aggregation.
     */
    protected abstract Aggregator createUnmapped(Aggregator parent, Map<String, Object> metadata) throws IOException;

    /**
     * Create the {@linkplain Aggregator}.
     * 
     * @param cardinality Upper bound of the number of {@code owningBucketOrd}s
     *                    that the {@link Aggregator} created by this method
     *                    will be asked to collect.
     */
    protected abstract Aggregator doCreateInternal(
        Map<String, ValuesSourceConfig> configs,
        DocValueFormat format,
        Aggregator parent,
        CardinalityUpperBound cardinality,
        Map<String, Object> metadata
    ) throws IOException;

}
