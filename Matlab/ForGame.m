experience=[100];
overall=100;
for finalLevel=2:1:100
    if finalLevel<50
        temp=experience(finalLevel-1)*1.3-(experience(finalLevel-1)*0.05);
    elseif finalLevel<75
        temp=experience(finalLevel-1)*1.5-(experience(finalLevel-1)*0.05);    
    elseif finalLevel<90
        temp=experience(finalLevel-1)*1.1-(experience(finalLevel-1)*0.05);    
    else
        temp=experience(finalLevel-1)*1.1-(experience(finalLevel-1)*0.05);    
    end
    overall=overall+temp;
    experience(finalLevel)=temp;
end
level=1;
totalAssignmentExp=0;
new=0;
current=experience(level);

for noOfAssignments=1:1:(24*5*4)%later-(4+4+4)for exam period, sat and sun already excluded
    
    if level==30
        fprintf('1');
        level=50;
    elseif level==85
        fprintf('3');
        level=90;
    elseif level==94
        fprintf('4');
        level=96;
    elseif level==98
        fprintf('5');
        level=100;
    end
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
