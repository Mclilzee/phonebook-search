package project.searchable;

import java.time.Duration;
import java.util.Arrays;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.counting;

abstract class Searcher {

    final Record[] searchableRecords;
    final Record[] toFind;
    Duration searchDuration = Duration.ZERO;
    Duration sortingDuration = Duration.ZERO;
    private int found = 0;

    Searcher(Record[] searchableRecords, Record[] toFind) {
        this.searchableRecords = searchableRecords;
        this.toFind = toFind;
    }

    public Duration getSearchDuration() {
        return this.searchDuration;
    }

    public void setSearchDuration(Duration start, Duration end) {
        this.searchDuration = end.minus(start);
    }

    public Duration getSortingDuration() {
        return this.sortingDuration;
    }

    void setSortingDuration(Duration start, Duration end) {
        this.sortingDuration = end.minus(start);
    }

    int getFound() {
        return this.found;
    }

    void findElements() {
        this.found = Arrays.stream(toFind)
                .filter(this::isElementInSearchableFile)
                .collect(collectingAndThen(counting(), Long::intValue));
    }

    String getFoundMessage() {
        String durationString = getDurationString(searchDuration.plus(sortingDuration));

        return String.format("Found %d / %d entries. Time taken: %s",
                this.found, this.toFind.length, durationString);
    }

    String getDurationString(Duration duration) {
        return String.format("%d min. %d sec. %d ms.", duration.toMinutesPart(), duration.toSecondsPart(), duration.toMillisPart());
    }

    abstract String search();

    abstract boolean isElementInSearchableFile(Record element);
}
