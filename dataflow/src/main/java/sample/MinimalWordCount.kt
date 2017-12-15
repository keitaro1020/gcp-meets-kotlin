/*
 * Copyright (C) 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package sample

import com.google.cloud.dataflow.sdk.Pipeline
import com.google.cloud.dataflow.sdk.io.TextIO
import com.google.cloud.dataflow.sdk.options.DataflowPipelineOptions
import com.google.cloud.dataflow.sdk.options.PipelineOptionsFactory
import com.google.cloud.dataflow.sdk.runners.BlockingDataflowPipelineRunner
import com.google.cloud.dataflow.sdk.transforms.Count
import com.google.cloud.dataflow.sdk.transforms.Filter
import com.google.cloud.dataflow.sdk.transforms.FlatMapElements
import com.google.cloud.dataflow.sdk.transforms.MapElements
import com.google.cloud.dataflow.sdk.transforms.SerializableFunction
import com.google.cloud.dataflow.sdk.values.KV
import com.google.cloud.dataflow.sdk.values.TypeDescriptor

import java.util.Arrays

fun main(args: Array<String>) {
    val options = PipelineOptionsFactory.create()
            .`as`(DataflowPipelineOptions::class.java)

    options.runner = BlockingDataflowPipelineRunner::class.java

    // CHANGE 1 of 3: Your project ID is required in order to run your pipeline on the Google Cloud.
    options.project = "SET_YOUR_PROJECT_ID_HERE"

    // CHANGE 2 of 3: Your Google Cloud Storage path is required for staging local files.
    options.stagingLocation = "gs://SET_YOUR_BUCKET_NAME_HERE/AND_STAGING_DIRECTORY"

    val p = Pipeline.create(options)

    p.apply(TextIO.Read.from("gs://dataflow-samples/shakespeare/*"))
            .apply(FlatMapElements.via { word: String -> Arrays.asList(*word.split("[^a-zA-Z']+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) }
                    .withOutputType(object : TypeDescriptor<String>() {

                    }))
            .apply(Filter.byPredicate(SerializableFunction { word: String -> !word.isEmpty() }))
            .apply(Count.perElement<String>())
            .apply(MapElements
                    .via { wordCount: KV<String, Long> -> wordCount.key + ": " + wordCount.value }
                    .withOutputType(object : TypeDescriptor<String>() {

                    }))

            // CHANGE 3 of 3: The Google Cloud Storage path is required for outputting the results to.
            .apply(TextIO.Write.to("gs://SET_YOUR_BUCKET_NAME_HERE/AND_STAGING_DIRECTORY"))

    p.run()
}
