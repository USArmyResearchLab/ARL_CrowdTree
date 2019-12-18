Stephen Tratz
October 2019
Crowdsourced dependency tree annotations

## Introduction
The CoNLL-U file in this directory contains the data collected in the crowdsourcing experiments described in Tratz (2019). Please see the paper for more details.

```
Tratz, Stephen. 2019. Dependency Tree Annotation with Mechanical Turk. In the Proceedings of the Aggregating and analysing crowdsourced annotations for NLP (AnnoNLP) workshop.
```

For more information on the annotation interface used to collect the dependency tree annotations, please see the following paper instead.

```
Stephen Tratz and Nhien Phan. 2018. A Web-based System for Crowd-in-the-Loop Dependency Treebanking. In the Proceedings of the Eleventh International Conference on Language Resources and Evaluation (LREC 2018).
```

### Data

The original sentences in this collection come from two sources: the Penn Treebank (Marcus et al., 1993) and the Westbury Wikipedia corpus (Shaoul, 2010).

The Penn Treebank (Marcus et al., 1993) data are proprietary and, therefore, the token text and parts-of-speech have been removed. To restore these fields, you will need a copy of Penn Treebank (available from the Linguistic Data Consortium as catalog item LDC99T42).

As described in the paper, some tokens have been merged together. To keep track of this for the Penn Treebank data, the carrot '^' character is used. For example, the line given below indicates that the text for the 12th token includes the text for the next (13th) token. Thus, if the 12th and 13th tokens were 'Oct.' and '4', respectively, the two tokens should be combined to 'Oct. 4'.

```
12	^	_	_	_	_	11	dep	_	_
```

As another example, see the line below, which has three carrot characters together. This indicates that four words (1+3) have been merged together. In this example, the resulting string should be 'Aug. 31 , 1987'.

```
16	^^^	_	_	_	_	15	dep	_	_
```

#### Source identifiers
The *source_id* indicates the whether sentence came from the Penn Treebank or from the Wikipedia corpus. For example, a *source_id* of *wsj-0008.3* should be interpreted as the 4th sentence (indexing begins at zero) in the Penn Treebank file wsj_0008.mrg. If the *source_id* starts with 'wiki', then it is part of the Wikipedia data.




#### Annotator identifiers

Mechanical Turk annotator ids have been anonymized and appear as W1, W2, W3, etc. For the Penn Treebank, the 'REF' *annotator_id* refers to the output of an automatic consituent-to-dependency conversion process, and the 'author' annotations are my own. For the wikipedia data, the 'REF' annotations are my annotations as there is no pre-existing standard.

#### Miscellaneous

Due to a bug in the software, one sentence has fewer than 10 annotations.

### References

```
Mitchell P. Marcus, Mary A. Marcinkiewicz, and Beatrice Santorini. 1993. Building a Large Annotated Corpus of English: The Penn Treebank. Computational Linguistics, 19(2):330.

Cyrus Shaoul. 2010. The Westbury Lab Wikipedia Corpus. Edmonton, Alberta: University of Alberta.
```