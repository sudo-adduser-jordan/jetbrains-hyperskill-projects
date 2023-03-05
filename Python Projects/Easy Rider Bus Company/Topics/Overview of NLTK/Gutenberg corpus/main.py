# Don't download gutenberg corpus. Just import it
import nltk.corpus

number = int(input())

print(nltk.corpus.gutenberg.sents('whitman-leaves.txt')[number])

