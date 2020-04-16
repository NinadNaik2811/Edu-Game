clc;
experienceGood=[20];
experienceBad=[16];
overall=20;
%ForGood
%health, attack, attackDefence, magic, magicDefence, accuracy
theSword=zeros(100,6);
theSword(1,:)=[15 5 4 0 2 0.8];
theWizard=zeros(100,6);
theWizard(1,:)=[15 0 2 5 4 0.7];
theScientist=zeros(100,6);
theScientist(1,:)=[25 3 6 0 3 0.4];
theBard=zeros(100,6);
theBard(1,:)=[20 0 3 3 6 0.5];
for finalLevel=2:1:100
    experienceGood(finalLevel)=1.15*experienceGood(finalLevel-1)-(experienceGood(finalLevel-1)*0.1);
    overall=overall+experienceGood(finalLevel);
    for i=1:1:5
        theSword(finalLevel,i)=1.15*theSword(finalLevel-1,i)-(theSword(finalLevel-1,i)/10);
        theWizard(finalLevel,i)=1.15*theWizard(finalLevel-1,i)-(theWizard(finalLevel-1,i)/10);
        theScientist(finalLevel,i)=1.15*theScientist(finalLevel-1,i)-(theScientist(finalLevel-1,i)/10);
        theBard(finalLevel,i)=1.15*theBard(finalLevel-1,i)-(theBard(finalLevel-1,i)/10);    
    end
end 

%ForBad
%health, attack, attackDefence, magic, magicDefence, accuracy
theBadSword=zeros(100,6);
theBadSword(1,:)=[12 4 3 0 1 0.8];
theBadWizard=zeros(100,6);
theBadWizard(1,:)=[12 0 1 4 3 0.7];
theBadScientist=zeros(100,6);
theBadScientist(1,:)=[20 2 5 0 2 0.4];
theBadBard=zeros(100,6);
theBadBard(1,:)=[16 0 2 2 5 0.5];
for finalLevel=2:1:100
    if finalLevel<30
        experienceBad(finalLevel)=1.15*experienceBad(finalLevel-1)-(experienceBad(finalLevel-1)*0.1);
    elseif finalLevel<80
        experienceBad(finalLevel)=1.16*experienceBad(finalLevel-1)-(experienceBad(finalLevel-1)*0.1);
    else
        experienceBad(finalLevel)=1.13*experienceBad(finalLevel-1)-(experienceBad(finalLevel-1)*0.1);        
    end
    for i=1:1:5
        if finalLevel<30
            theBadSword(finalLevel,i)=1.15*theBadSword(finalLevel-1,i)-(theBadSword(finalLevel-1,i)*0.1);
            theBadWizard(finalLevel,i)=1.15*theBadWizard(finalLevel-1,i)-(theBadWizard(finalLevel-1,i)*0.1);
            theBadScientist(finalLevel,i)=1.15*theBadScientist(finalLevel-1,i)-(theBadScientist(finalLevel-1,i)*0.1);
            theBadBard(finalLevel,i)=1.15*theBadBard(finalLevel-1,i)-(theBadBard(finalLevel-1,i)*0.1);
        elseif finalLevel<80
            theBadSword(finalLevel,i)=1.16*theBadSword(finalLevel-1,i)-(theBadSword(finalLevel-1,i)*0.1);
            theBadWizard(finalLevel,i)=1.16*theBadWizard(finalLevel-1,i)-(theBadWizard(finalLevel-1,i)*0.1);
            theBadScientist(finalLevel,i)=1.16*theBadScientist(finalLevel-1,i)-(theBadScientist(finalLevel-1,i)*0.1);
            theBadBard(finalLevel,i)=1.16*theBadBard(finalLevel-1,i)-(theBadBard(finalLevel-1,i)*0.1);
        else
            theBadSword(finalLevel,i)=1.13*theBadSword(finalLevel-1,i)-(theBadSword(finalLevel-1,i)*0.1);
            theBadWizard(finalLevel,i)=1.13*theBadWizard(finalLevel-1,i)-(theBadWizard(finalLevel-1,i)*0.1);
            theBadScientist(finalLevel,i)=1.13*theBadScientist(finalLevel-1,i)-(theBadScientist(finalLevel-1,i)*0.1);
            theBadBard(finalLevel,i)=1.13*theBadBard(finalLevel-1,i)-(theBadBard(finalLevel-1,i)*0.1);
        end
    end
end 

%%Character Growth
if 0
    figure
    subplot(2,2,1)
    plot(1:1:100,theBadSword(:,5),'r',1:1:100,theSword(:,5),'b');
    title('Sword');
    xlabel('Level');
    ylabel('Magic Defense');
    subplot(2,2,2)
    plot(1:1:100,theBadWizard(:,5),'r',1:1:100,theWizard(:,5),'b');
    title('Wizard');
    xlabel('Level');
    ylabel('Magic Defense');
    subplot(2,2,3)
    plot(1:1:100,theBadScientist(:,5),'r',1:1:100,theScientist(:,5),'b');
    title('Scientist');
    xlabel('Level');
    ylabel('Magic Defense');
    subplot(2,2,4)
    plot(1:1:100,theBadBard(:,5),'r',1:1:100,theBard(:,5),'b');
    title('Bard');
    xlabel('Level');
    ylabel('Magic Defense');
end

%Exp vs level
if 0
    plot(1:1:100,experienceBad,'r',1:1:100,experienceGood,'b');
    title('Difficulty');
    xlabel('Level');
    ylabel('Experience');
end

%%WE WILL HAVE THE EXPERIENCE BROKEN DOWN AS:
%%Assignments-40% weightage and 48 per year out of 10 marks
%%UTs-30% weightage and 4 per Year out of 20 marks
%%End Semesters-30% weightage and 2 per Year out of 100 marks

assignmentExperience=0;
UTExperience=0;
EndSemExperience=0;

for assignment=2:1:100
    assignmentExperience=assignmentExperience+(1.15*experienceGood(assignment-1)-(experienceGood(assignment-1)*0.1))*0.4;
    UTExperience=UTExperience+(1.15*experienceGood(assignment-1)-(experienceGood(assignment-1)*0.1))*0.3;
    EndSemExperience=EndSemExperience+(1.15*experienceGood(assignment-1)-(experienceGood(assignment-1)*0.1))*0.3;
end
overall*0.4/48;
perAssignmentExperience=(assignmentExperience)/48;
perUTExperience=UTExperience/4;
perEndSemExperience=EndSemExperience/2;
