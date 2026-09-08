package org.clulab.utils

class TestThreading extends Test {
  val threads = 26
  val numbers = 0.until(threads)

  {
    val parNumbers = ThreadUtils.parallelize(numbers, threads)

    parNumbers.foreach { number =>
      println(number)
    }
  }

  class InspectableParallelizer[T](seq: Seq[T], threadLimit: Int)
      extends Parallelizer[T](seq, threadLimit) {
    def isShutdown: Boolean = forkJoinPool.isShutdown
  }

  behavior of "Parallelizer"

  it should "shut down its thread pool when closed" in {
    val parallelizer = new InspectableParallelizer(numbers, threads)

    try {
      parallelizer.par.sum should be(numbers.sum)
    }
    finally {
      parallelizer.close()
    }

    parallelizer.isShutdown should be(true)
  }
}
