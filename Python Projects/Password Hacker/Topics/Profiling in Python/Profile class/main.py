import cProfile, pstats, io

# instantiate and enable the profiler
profiler = cProfile.Profile()
profiler.enable()
def fib_list(n):
    if n < 2:
        return n
    sequence = [0, 1]
    for i in range(2, n + 1):
        sequence.append(sequence[i - 1] + sequence[i - 2])
    return sequence[n]
fib_list(40)

# disable the profiler
profiler.disable()
# prepare the output statistics
stream = io.StringIO()
stats = pstats.Stats(profiler, stream=stream).sort_stats('cumulative')
stats.print_stats()
output = stream.getvalue()