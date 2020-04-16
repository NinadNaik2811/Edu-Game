

    if finalLevel<=50
        temp=experience(finalLevel-1)*1.3;
    elseif finalLevel<=100
        temp=experience(finalLevel-1)*1.25;
    elseif finalLevel<=150
        temp=experience(finalLevel-1)*1.2;
    elseif finalLevel<=200
        temp=experience(finalLevel-1)*1.15;
    elseif finalLevel<=250
        temp=experience(finalLevel-1)*1.15;
    end
    
    if finalLevel<=100
        temp=experience(finalLevel-1)*1.25-(experience(finalLevel-1)*0.05);
    elseif finalLevel<=125
        temp=experience(finalLevel-1)*1.2-(experience(finalLevel-1)*0.10);
    elseif finalLevel<=150
        temp=experience(finalLevel-1)*1.1-(experience(finalLevel-1)*0.10);
    elseif finalLevel<=200 
        temp=experience(finalLevel-1)*1-(experience(finalLevel-1)*0.15);
    elseif finalLevel<=225 
        temp=experience(finalLevel-1)*0.9+(experience(finalLevel-1)*0.05);
    else
        temp=experience(finalLevel-1)*0.85+(experience(finalLevel-1)*0.05);


    
    %First Sem
weeks=0.1:24/100:24;
subplot(5,2,1)
plot(weeks,experience(1:1:100))

%Second Sem
weeks=0.1:24/100:24;
subplot(5,2,2)
plot(weeks,experience(101:1:200))

%Overall Sem
weeks=1:240/251:240;
subplot(5,1,3)
plot(weeks,experience)

subplot(5,1,4)
plot(1:1:250,experience)