git log --since="30 days" --all --pretty=format:"%cd|%s" --date=short | awk -F "|" '{if (old != $1) {print $1"|"$2;} old = $1;}'
