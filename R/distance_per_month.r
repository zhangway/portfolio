df <- read.csv("C:/Users/zhangwei/workspace/R/cardioActivities.csv", sep=",", header=TRUE)

yearmon <- as.Date(df$Date, "%Y-%m-%d")

df$Date <- format(yearmon, format="%Y%m")

print(df)

data <- aggregate(Distance ~ Date, df, function(x) sum=sum(x))

mp <- barplot(data$Distance, main="Distance Plot", xlab="Month", ylab="Distance", ylim=c(0,50), names.arg=data$Date)



mydates <- as.Date(as.POSIXlt(strptime(df$Date, "%m/%d/%Y %H:%M")))



df$Date <- format(mydates, format="%m")



#format(df$Date, format="%B")


#date <- as.Date(as.POSIXlt(strptime(df$Date, "%m/%d/%Y %H:%M")))

#date

#df$Date <- date$mon+1

#format(df$Date, format="%B")



data <- aggregate(Distance ~ Date, df, function(x) sum=sum(x))

df2 <- data[order(data$Date),]

dd <- as.Date(as.POSIXlt(strptime(df2$Date, "%m")))

dd


mp <- barplot(data$Distance, main="Distance Plot", xlab="Month", ylab="Distance", ylim=c(0,70), names.arg=df2$Date)

