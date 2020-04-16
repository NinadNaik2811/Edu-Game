experience=[100];
overall=100;
tillAssignmentAvailable1=0;
tillAssignmentAvailable2=0;
tillAssignmentAvailable3=0;
tillAssignmentAvailable4=0;
tillAssignmentAvailable5=0;
for finalLevel=2:1:100
    temp=experience(finalLevel-1)*1.20-(experience(finalLevel-1)*0.05);
    overall=overall+temp;
    if finalLevel==50
        tillAssignmentAvailable1=overall;
    elseif finalLevel==75
        tillAssignmentAvailable2=overall-tillAssignmentAvailable1;
    elseif finalLevel==90
        tillAssignmentAvailable3=overall-(tillAssignmentAvailable1+tillAssignmentAvailable2);
    elseif finalLevel==96
        tillAssignmentAvailable4=overall-(tillAssignmentAvailable1+tillAssignmentAvailable2+tillAssignmentAvailable3);
    elseif finalLevel==100
        tillAssignmentAvailable5=overall-(tillAssignmentAvailable1+tillAssignmentAvailable2+tillAssignmentAvailable3+tillAssignmentAvailable4);
    end
    experience(finalLevel)=temp;
end
level=1;
totalAssignmentExp=0;
new=0;
current=experience(level);

for noOfAssignments=1:1:(24*5*10)%later-(4+4+4)for exam period, sat and sun already excluded

    assignmentEarned=(10+level)^2;%10 is max marks
    totalAssignmentExp=totalAssignmentExp+assignmentEarned;
    if current<assignmentEarned
        new=assignmentEarned-current;
        level=level+1;
        current=experience(level)-new;
    elseif current==assignmentEarned
        new=0;
        level=level+1;
    else
        current=current-assignmentEarned;
    end
    
end

plot(1:1:100,experience);
