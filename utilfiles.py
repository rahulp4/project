def plot_history(history):
  hist = pd.DataFrame(history.history)
  hist['epoch'] = history.epoch
  
  plt.figure()
  plt.xlabel('Epoch')
  plt.ylabel('Mean Abs Error [MPG]')
  plt.plot(hist['epoch'], hist['mean_absolute_error'],
           label='Train Error')
  plt.plot(hist['epoch'], hist['val_mean_absolute_error'],
           label = 'Val Error')
  plt.ylim([0,5])
  plt.legend()
  
  plt.figure()
  plt.xlabel('Epoch')
  plt.ylabel('Mean Square Error [$MPG^2$]')
  plt.plot(hist['epoch'], hist['mean_squared_error'],
           label='Train Error')
  plt.plot(hist['epoch'], hist['val_mean_squared_error'],
           label = 'Val Error')
  plt.ylim([0,20])
  plt.legend()
  plt.show()

def resultDF(df):
  resultdf = X_test
  resultdf['SalePrice'] = hist
  # resultdf['GrLivArea'] = resultdf['GrLivArea'] * 2000

  print(resultdf)
  #loss, accuracy = model.evaluate(master_data_train,final_clean_data_train_label, verbose=0)
  #print('Accuracy: %f' % (accuracy))
  #print('Loss: %f' % (loss))
  
  fig, ax = plt.subplots()
  ax.scatter(x = resultdf['GrLivArea'], y = resultdf['SalePrice'])
  plt.ylabel('SalePrice', fontsize=13)
  plt.xlabel('GrLivArea', fontsize=13)
  plt.show()
  
  
  
class myCallback(tf.keras.callbacks.Callback):
  def on_epoch_end(self, epoch, logs={}):
    if(logs.get('acc')>0.6):
      print("\nReached 60% accuracy so cancelling training!")
      self.model.stop_training = True  
	  
	  
import seaborn as sns	  
def pairPlot():
	# Seaborn visualization library
	
	# Create the default pairplot
	sns.pairplot(df)